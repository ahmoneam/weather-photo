package com.ahmoneam.basecleanarchitecture.base.platform

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ahmoneam.basecleanarchitecture.Result
import com.ahmoneam.basecleanarchitecture.utils.EventObserver
import com.tapadoo.alerter.Alerter
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseDialogFragment<ViewModel : BaseViewModel> : DialogFragment() {

    val viewModel: ViewModel by lazy { getViewModel(viewModelClass()) }

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
