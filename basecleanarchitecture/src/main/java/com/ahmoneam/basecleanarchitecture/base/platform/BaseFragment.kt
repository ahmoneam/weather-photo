package com.ahmoneam.basecleanarchitecture.base.platform

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.ahmoneam.basecleanarchitecture.Result
import com.ahmoneam.basecleanarchitecture.utils.EventObserver
import com.tapadoo.alerter.Alerter
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseFragment<ViewModel : BaseViewModel>
    : Fragment() {

    open val viewModel: ViewModel by lazy { getViewModel(viewModelClass()) }

    @Suppress("UNCHECKED_CAST")
    private fun viewModelClass(): KClass<ViewModel> {
        // dirty hack to get generic type https://stackoverflow.com/a/1901275/719212
        return ((javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<ViewModel>).kotlin
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initGenericObservers()
    }

    private fun initGenericObservers() {
        initLoading(viewModel)
        initError(viewModel)
        initNavigation(viewModel)
    }

    private fun initNavigation(vm: BaseViewModel) {
        vm.nextScreen.observe(this, EventObserver {
            handleNavigation(it)
        })
    }

    private fun initError(vm: BaseViewModel) {
        vm.error.observe(this, EventObserver {
            hideLoading()
            showError(it)
        })
    }

    private fun initLoading(vm: BaseViewModel) {
        vm.loading.observe(this, EventObserver {
            if (it.loading) showLoading()
            else hideLoading()
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
        hideKeyboard()
        Alerter.create(activity!!)
            .enableProgress(true)
            .show()
//        Toast.makeText(activity!!, "loading", Toast.LENGTH_SHORT).show()
    }

    fun hideKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
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
