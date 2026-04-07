package com.foodsaver.app.commonModule.apiResult

import com.foodsaver.app.commonModule.dto.GlobalErrorResponse
import com.foodsaver.app.commonModule.utils.uiText.LocalError
import com.foodsaver.app.commonModule.utils.uiText.UiText
import com.foodsaver.app.commonModule.utils.uiText.asUiText

sealed class ApiResult<out T> {

    data class Success<out T>(val data: T) : ApiResult<T>()

    sealed class Error : ApiResult<Nothing>() {

        abstract val uiText: UiText

        data class Http(val errorResponse: GlobalErrorResponse) : Error() {
            override val uiText: UiText
                get() = errorResponse.asUiText()
        }

        data class Local(val localError: LocalError<*>) : Error() {
            override val uiText: UiText
                get() = localError.asUiText()
        }
    }

    data object Loading : ApiResult<Nothing>()
    data object Idle: ApiResult<Nothing>()

    companion object {
        fun <T> success(value: T): ApiResult<T> = Success(value)
        fun error(message: String): ApiResult<Nothing> = Error.Http(
            errorResponse = GlobalErrorResponse(
                error = "API error",
                message = message,
                httpCode = 0
            )
        )

        fun error(error: GlobalErrorResponse): ApiResult<Nothing> = Error.Http(error)

        fun localError(localError: LocalError<*>): ApiResult<Nothing> =
            Error.Local(localError)

        fun loading(): ApiResult<Nothing> = Loading
    }
}