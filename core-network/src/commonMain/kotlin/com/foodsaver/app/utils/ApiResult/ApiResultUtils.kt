package com.foodsaver.app.utils.ApiResult

import com.foodsaver.app.dto.GlobalErrorResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun <T: Any, R: Any> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiResult.Error -> this
        ApiResult.Loading -> ApiResult.Loading
        is ApiResult.Success -> ApiResult.Success(transform(this.data))
    }
}

fun <T: Any> ApiResult<T>.onFailure(action: (GlobalErrorResponse) -> Unit): ApiResult<T> {
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

inline fun <T: Any> ApiResult<T>.onSuccess(scope: CoroutineScope, crossinline action: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        scope.launch {
            action(this@onSuccess.data)
        }
    }

    Result
    return this
}

inline fun<T> ApiResult<T>.getOrElse(onFailure: (GlobalErrorResponse?) -> T): T {
    return when (this) {
        is ApiResult.Error -> onFailure(this.error)
        ApiResult.Loading -> onFailure(null)
        is ApiResult.Success<T> -> this.data
    }
}