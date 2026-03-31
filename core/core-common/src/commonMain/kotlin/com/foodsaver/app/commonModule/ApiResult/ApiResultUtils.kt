package com.foodsaver.app.commonModule.ApiResult

import com.foodsaver.app.commonModule.dto.GlobalErrorResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun <T: Any, R: Any> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiResult.Error -> this
        ApiResult.Loading -> ApiResult.Loading
        is ApiResult.Success -> ApiResult.Success(transform(this.data))
    }
}
fun <T, R> ApiResult<T?>.mapNullable(transform: (T?) -> R): ApiResult<R?> {
    return when (this) {
        is ApiResult.Error -> this
        ApiResult.Loading -> ApiResult.Loading
        is ApiResult.Success -> ApiResult.Success(transform(this.data))
    }
}

suspend fun <T> ApiResult<T?>.onFailureNullable(action: suspend (GlobalErrorResponse) -> Unit): ApiResult<T?> {
    if (this is ApiResult.Error) {
        action(this.error)
    }

    return this
}

suspend fun <T: Any> ApiResult<T>.onFailure(action: suspend (GlobalErrorResponse) -> Unit): ApiResult<T> {
    if (this is ApiResult.Error) {
        action(this.error)
    }

    return this
}

suspend fun <T: Any> ApiResult<T>.onSuccess(action: suspend (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        action(this.data)
    }

    return this
}
suspend fun <T> ApiResult<T?>.onSuccessNullable(action: suspend (T?) -> Unit): ApiResult<T?> {
    if (this is ApiResult.Success) {
        action(this.data)
    }

    return this
}

inline fun <T: Any> ApiResult<T>.onSuccess(scope: CoroutineScope, crossinline action: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        scope.launch {
            action(this@onSuccess.data)
        }
    }

    return this
}

inline fun<T> ApiResult<T>.getOrElse(onFailure: (GlobalErrorResponse?) -> T): T {
    return when (this) {
        is ApiResult.Error -> onFailure(this.error)
        ApiResult.Loading -> onFailure(null)
        is ApiResult.Success<T> -> this.data
    }
}

fun<T> ApiResult<T>.failure(e: Exception): ApiResult<T> {

    if (this is ApiResult.Error) return this

    return ApiResult.Error(
        error = GlobalErrorResponse(
            error = e::class.simpleName ?: "Unknown error",
            message = e.message ?: "Unknown error",
            httpCode = 0,
            errorCode = 0
        )
    )
}