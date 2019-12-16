package com.ahmoneam.basecleanarchitecture.base.platform

import com.ahmoneam.basecleanarchitecture.R
import com.ahmoneam.basecleanarchitecture.Result
import com.ahmoneam.basecleanarchitecture.base.data.local.LocalDataSource
import com.ahmoneam.basecleanarchitecture.base.data.remote.RemoteDataSource
import com.ahmoneam.basecleanarchitecture.utils.ApplicationException
import com.ahmoneam.basecleanarchitecture.utils.ErrorType
import com.ahmoneam.basecleanarchitecture.utils.IConnectivityUtils
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.inject
import retrofit2.Response

abstract class BaseRepository<LocalData : LocalDataSource, RemoteData : RemoteDataSource>
    (val localDataSource: LocalData, val remoteDataSource: RemoteData) : IBaseRepository {

    private val connectivityUtils: IConnectivityUtils by inject()
    private val gson: Gson by inject()

    private val noInternetError = Result.Error(
        ApplicationException(
            type = ErrorType.Network.NoInternetConnection,
            errorMessageRes = R.string.error_no_internet_connection
        )
    )
    val unexpectedError = Result.Error(
        ApplicationException(
            type = ErrorType.Network.Unexpected,
            errorMessageRes = R.string.error_no_internet_connection
        )
    )

    override suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                // check internet connection
                if (connectivityUtils.isNetworkConnected.not()) return@withContext noInternetError

                // make api call
                val response = call()

                // check response and convert to result
                return@withContext handleResult(response)

            } catch (error: Throwable) {
                unexpectedError(error)
            }
        }
    }

    override suspend fun <T : Any> safeDeferredApiCall(call: () -> Deferred<Response<T>>): Result<T> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                // check internet connection
                if (connectivityUtils.isNetworkConnected.not()) return@withContext noInternetError

                // make api call
                val response = call().await()

                // check response and convert to result
                return@withContext handleResult(response)
            } catch (error: Throwable) {
                unexpectedError(error)
            }
        }
    }

    fun unexpectedError(error: Throwable): Result.Error {
        return Result.Error(
            ApplicationException(
                throwable = error,
                type = ErrorType.Network.Unexpected
            )
        )
    }

    private fun <T : Any> handleResult(response: Response<T>): Result<T> {
        return when (response.code()) {
            in 1..399 -> Result.Success(response.body()!!)
            401 -> Result.Error(
                ApplicationException(
                    type = ErrorType.Network.Unauthorized,
                    errorMessage = "Unauthorized"
                )
            )
            404 -> Result.Error(
                ApplicationException(
                    type = ErrorType.Network.ResourceNotFound,
                    errorMessage = "ResourceNotFound"
                )
            )
            else -> Result.Error(
                ApplicationException(
                    type = ErrorType.Network.Unexpected,
                    errorMessage = "Unexpected"
                )
            )
        }
    }
}