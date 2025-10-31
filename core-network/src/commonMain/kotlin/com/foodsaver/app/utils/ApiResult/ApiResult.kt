package com.foodsaver.app.utils.ApiResult

import com.foodsaver.app.dto.GlobalErrorResponse

sealed interface ApiResult<out T> {

    data class Success<T>(val data: T): ApiResult<T>
    data class Error(val error: GlobalErrorResponse): ApiResult<Nothing>

    data object Loading: ApiResult<Nothing>
}