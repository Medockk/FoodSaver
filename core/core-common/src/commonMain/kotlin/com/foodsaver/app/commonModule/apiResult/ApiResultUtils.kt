package com.foodsaver.app.commonModule.apiResult

import com.foodsaver.app.commonModule.dto.GlobalErrorResponse

inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiResult.Error -> this
        ApiResult.Loading -> ApiResult.Loading
        is ApiResult.Success -> ApiResult.Success(transform(this.data))
        ApiResult.Idle -> ApiResult.Idle
    }
}

suspend inline fun <T> ApiResult<T>.onFailure(
    action: suspend (ApiResult.Error) -> Unit
): ApiResult<T> {
    if (this is ApiResult.Error) {
        action(this)
    }

    return this
}

suspend inline fun <T> ApiResult<T>.onLocalFailure(
    action: suspend (ApiResult.Error.Local) -> Unit
): ApiResult<T> {
    if (this is ApiResult.Error.Local) {
        action(this)
    }

    return this
}

suspend inline fun <T> ApiResult<T>.onSuccess(action: suspend (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        action(this.data)
    }

    return this
}

inline fun <T> ApiResult<T>.onLoading(action: () -> Unit) : ApiResult<T> {
    if (this is ApiResult.Loading) {
        action()
    }

    return this
}

inline fun<T> ApiResult<T>.getOrElse(onFailure: (ApiResult.Error?) -> T): T {
    return when (this) {
        is ApiResult.Success<T> -> this.data
        else -> onFailure(null)
    }
}

fun handleError(e: Exception): GlobalErrorResponse {
    return GlobalErrorResponse(
        error = e::class.simpleName ?: "Unknown error",
        message = e.message ?: "Unknown error",
        httpCode = 0
    )
}