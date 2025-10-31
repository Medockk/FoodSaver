package com.foodsaver.app.utils.ApiResult

import com.foodsaver.app.dto.GlobalErrorResponse

inline fun <T: Any> ApiResult<T>.onFailure(action: (GlobalErrorResponse) -> Unit): ApiResult<T> {
    if (this is ApiResult.Error) {
        action(this.error)
    }

    return this
}