package com.foodsaver.app.commonModule.ApiResult

import com.foodsaver.app.commonModule.dto.GlobalErrorResponse

value class NewApiResult<out T> internal constructor(
    internal val value: T
) {

    val isSuccess: Boolean
        get() = value !is Error
    val isFailure: Boolean
        get() = value is Error

    companion object {
        inline fun<T> success(value: T): NewApiResult<T> {
            return NewApiResult(value)
        }
    }

    object Loading

    class Error(
        val exception: GlobalErrorResponse
    ) {
        override fun toString(): String ="Error($exception)"
        override fun equals(other: Any?): Boolean = other is NewApiResult<*>.Error && other.exception == exception
        override fun hashCode(): Int = exception.hashCode()
    }
}