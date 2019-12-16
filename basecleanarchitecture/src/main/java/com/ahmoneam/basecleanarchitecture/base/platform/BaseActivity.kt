package com.ahmoneam.basecleanarchitecture.base.platform

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ahmoneam.basecleanarchitecture.Result
import com.ahmoneam.basecleanarchitecture.utils.EventObserver
import com.ahmoneam.basecleanarchitecture.utils.LocaleHelper
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseActivity<ViewModel : BaseViewModel> : AppCompatActivity() {

    val viewModel: ViewModel by lazy { getViewModel(viewModelClass()) }

    @Suppress("UNCHECKED_CAST")
    private fun viewModelClass(): KClass<ViewModel> {
        // dirty hack to get generic type https://stackoverflow.com/a/1901275/719212
        return ((javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<ViewModel>).kotlin
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loading.observe(this, EventObserver {
            if (it.loading) showLoading()
            else hideLoading()
        })
        viewModel.error.observe(this, EventObserver {
            hideLoading()
            showError(it)
        })
        viewModel.nextScreen.observe(this, EventObserver {
            when (it) {
                is BaseNavigationDestination.Activities -> it.start(this)
            }
        })
    }

    open fun showError(error: Result.Error) {
//        TODO()
    }

    open fun hideLoading() {
//        TODO()
    }

    open fun showLoading() {
//        TODO()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        LocaleHelper.onAttach(this)
        super.onConfigurationChanged(newConfig)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!))
    }
}
