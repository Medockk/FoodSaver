package com.foodsaver.app.utils.ApiResult

inline fun <T: Any, R: Any> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiResult.Error -> this
        ApiResult.Loading -> ApiResult.Loading
        is ApiResult.Success -> ApiResult.Success(transform(this.data))
    }
}