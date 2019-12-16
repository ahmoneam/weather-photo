package com.moneam.weatherphoto.feature.list

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ahmoneam.basecleanarchitecture.base.platform.BaseFragment
import com.ahmoneam.basecleanarchitecture.utils.shareImageUri
import com.moneam.weatherphoto.R
import kotlinx.android.synthetic.main.fragment_first.*

class HomeFragment : BaseFragment<HomeViewModel>() {

    private lateinit var adapter: PhotosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        floatingActionButtonAdd.setOnClickListener {
            findNavController().navigate(R.id.SecondFragment)
        }
        viewModel.photos.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.start()
    }

    private fun initView() {
        adapter = PhotosAdapter {
            requireActivity().shareImageUri(Uri.parse(it))
        }
        photosRecyclerView.adapter = adapter
    }
}
