package com.foodsaver.app.utils.ApiResult

inline fun <T: Any> ApiResult<T>.onSuccess(action: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        action(this.data)
    }

    return this
}