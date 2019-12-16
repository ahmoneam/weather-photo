package com.ahmoneam.basecleanarchitecture.base.platform

import androidx.lifecycle.*
import com.ahmoneam.basecleanarchitecture.Result
import com.ahmoneam.basecleanarchitecture.utils.ApplicationException
import com.ahmoneam.basecleanarchitecture.utils.ErrorType
import com.ahmoneam.basecleanarchitecture.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {
    val error = MutableLiveData<Event<Result.Error>>()
    val loading =
        MutableLiveData<Event<Result.Loading>>().apply { value = Event(Result.Loading(false)) }
    val nextScreen = MutableLiveData<Event<BaseNavigationDestination<*, *>>>()

    inline fun wrapBlockingOperation(
        showLoading: Boolean = true,
        crossinline function: suspend CoroutineScope.() -> Unit
    ): Job {
        loading.value = Event(Result.Loading(showLoading))
        return viewModelScope.launch {
            try {
                function()
            } catch (throwable: Throwable) {
                handelError(throwable)
                Timber.e(throwable)
            } finally {
                loading.value = Event(Result.Loading(false))
            }
        }
    }

    inline fun <reified T> wrapBlockingOperationLiveData(
        showLoading: Boolean = true,
        defaultOnError: T? = null,
        crossinline function: suspend () -> T
    ): LiveData<T> {
        loading.value = Event(Result.Loading(showLoading))
        return liveData {
            try {
                emit(function())
            } catch (throwable: Throwable) {
                handelError(throwable)
                defaultOnError?.let { emit(it) }
                Timber.e(throwable)
            } finally {
                loading.value = Event(Result.Loading(false))
            }
        }
    }

    fun <T> handleResult(result: Result<T>, onSuccess: (Result.Success<T>) -> Unit) {
        when (result) {
            is Result.Success<T> -> {
                onSuccess(result)
            }
            is Result.Error -> {
                throw result.exception
            }
        }
    }

    fun <T> handleResultLiveData(
        result: Result<T>,
        onError: ((Exception) -> Unit)? = null
    ): Result.Success<T> {
        when (result) {
            is Result.Success<T> -> {
                return result
            }
            is Result.Error -> {
                onError?.invoke(result.exception)
                throw result.exception
            }
            else -> throw ApplicationException(ErrorType.Unexpected)
        }
    }

    suspend fun <T> handleResultSuspend(
        result: Result<T>,
        onSuccess: suspend (Result.Success<T>) -> Unit
    ) {
        when (result) {
            is Result.Success<T> -> {
                onSuccess(result)
            }
            is Result.Error -> {
                throw result.exception
            }
        }
    }

    fun handelError(throwable: Throwable) {
        if (throwable is ApplicationException) {
            error.postValue(Event(Result.Error(throwable)))

//            when (throwable.type) {
//                ErrorType.Network.Unauthorized -> TODO()
//                ErrorType.Network.ResourceNotFound -> TODO()
//                ErrorType.Network.Unexpected -> TODO()
//                ErrorType.Network.NoInternetConnection -> TODO()
//                else -> {
//                }
//            }
        }
//        TODO()
    }
}