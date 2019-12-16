package com.moneam.weatherphoto.feature.photo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ahmoneam.basecleanarchitecture.base.platform.BaseFragment
import com.ahmoneam.basecleanarchitecture.utils.EventObserver
import com.birjuvachhani.locus.Locus
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.moneam.weatherphoto.BuildConfig
import com.moneam.weatherphoto.R
import com.moneam.weatherphoto.databinding.FragmentPhotoBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_photo.*
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File


class PhotoFragment : BaseFragment<PhotoViewModel>() {
    private lateinit var binding: FragmentPhotoBinding

    private fun checkGooglePlayServices(activity: Activity?): Boolean {
        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, 9000).show()
            }
            return false
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentPhotoBinding>(
            inflater,
            R.layout.fragment_photo,
            container,
            false
        )
        return binding.apply {
            vm = viewModel
            lifecycleOwner = this@PhotoFragment
            executePendingBindings()
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.startCamera.observe(this, EventObserver { startCamera() })
        captureButton.setOnClickListener { startLocation() }
        shareButton.setOnClickListener { shareImage() }
    }

    private fun startLocation() {
        if (!checkGooglePlayServices(requireActivity())) return
        Locus.setLogging(BuildConfig.DEBUG)
        Locus.getCurrentLocation(requireContext()) { result ->
            result.location?.let {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                viewModel.updateLocation(it.latitude, it.longitude)
            }
            result.error?.let {
                Timber.e(it)
                /* Received error! todo */
            }
        }
    }

    private fun startCamera() {
        ImagePicker.with(this)
            .cameraOnly()       //User can only capture image using Camera
            .crop(16f, 9f)      //Crop image with 16:9 aspect ratio
            .compress(1024)     //Final image size will be less than 1 MB(Optional)
            .start { resultCode, data ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        //Image Uri will not be null for RESULT_OK
                        val fileUri = data?.data
                        viewModel.imageUpdated(fileUri.toString())
                        Picasso.get()
                            .load(fileUri)
                            .fit()
                            .into(photoImageView)

                        //You can get File object from intent
                        val file: File? = ImagePicker.getFile(data)

                        //You can also get File Path from intent
                        val filePath: String? = ImagePicker.getFilePath(data)
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {
                        Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun shareImage() {
        loadBitmapFromView(photoFrameLayout) { bitmap ->
            val uri = getImageUri(bitmap)
            uri?.let {
                viewModel.onShareImageClick(uri.toString())
                shareImageUri(it)
            }
        }
    }

    private fun shareImageUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/jpg"
        startActivity(intent)
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun loadBitmapFromView(v: View, onSuccess: (Bitmap) -> Unit) {
        v.post {
            val width = v.width
            val height = v.height
            val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.draw(c)
            onSuccess(b)
        }
    }
}
