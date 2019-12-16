package com.ahmoneam.basecleanarchitecture.base.platform

import android.os.Bundle
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelStoreOwner
import com.ahmoneam.basecleanarchitecture.R
import com.ahmoneam.basecleanarchitecture.Result
import com.ahmoneam.basecleanarchitecture.utils.EventObserver
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tapadoo.alerter.Alerter
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseBottomSheetDialogFragment<ViewModel : BaseViewModel> :
    BottomSheetDialogFragment() {

    val viewModel: ViewModel by lazy { getSharedViewModel(viewModelClass(), from = getFrom()) }

    open fun getFrom(): () -> ViewModelStoreOwner = { activity as ViewModelStoreOwner }

    @Suppress("UNCHECKED_CAST")
    private fun viewModelClass(): KClass<ViewModel> {
        // dirty hack to get generic type https://stackoverflow.com/a/1901275/719212
        return ((javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<ViewModel>).kotlin
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.loading.observe(this, EventObserver {
            if (it.loading) showLoading()
            else hideLoading()
        })

        viewModel.error.observe(this, EventObserver {
            hideLoading()
            showError(it)
        })

        viewModel.nextScreen.observe(this, EventObserver {
            handleNavigation(it)
        })
    }

    fun expandDialogToMatchContent() {
        val d = dialog as BottomSheetDialog
        val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)!!
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    open fun showError(error: Result.Error) {
        val errorMessage = error.exception.errorMessage
            ?: run {
                return@run if (error.exception.errorMessageRes != null) {
                    getString(error.exception.errorMessageRes)
                } else null
            }
            ?: "unexpected error"
        if (errorMessage.isNotEmpty()) {
//            Toast.makeText(activity!!, errorMessage, Toast.LENGTH_LONG).show()
            Alerter.create(activity!!)
                .setTitle("Error")
                .setText(errorMessage)
                .show()
        }
    }

    open fun hideLoading() {
//        Toast.makeText(activity!!, "loaded", Toast.LENGTH_SHORT).show()
    }

    open fun showLoading() {
        Alerter.create(activity!!)
            .enableProgress(true)
            .show()
//        Toast.makeText(activity!!, "loading", Toast.LENGTH_SHORT).show()
    }

    open fun handleNavigation(it: BaseNavigationDestination<*, *>) {
        when (it) {
            is BaseNavigationDestination.Activities -> it.start(activity!!)
            is BaseNavigationDestination.Fragments -> it.start(this)
            else -> {
                Timber.e("Unsupported type ")
            }
        }
    }
}
