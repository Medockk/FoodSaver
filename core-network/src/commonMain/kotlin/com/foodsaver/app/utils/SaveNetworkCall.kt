package com.foodsaver.app.utils

import com.foodsaver.app.dto.GlobalErrorResponse
import com.foodsaver.app.utils.ApiResult.ApiResult
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

suspend inline fun<reified T> saveNetworkCall(
    crossinline action: suspend () -> HttpResponse
) : ApiResult<T> {
    return try {
        val result = action.invoke()

        if (result.status.isSuccess()) {
            ApiResult.Success(result.body())
        } else {
            val errorBodyText = result.bodyAsText()

            val errorResult = runCatching {
                Json.decodeFromString<GlobalErrorResponse>(errorBodyText)
            }.getOrElse {
                return ApiResult.Error(
                    error = GlobalErrorResponse(
                        error = "Server error: ${result.status.value}",
                        message = "Failed to serialize body with ${it.message}",
                        httpCode = 0
                    )
                )
            }
            ApiResult.Error(errorResult)
        }
    } catch (e: Exception) {
        ApiResult.Error(
            GlobalErrorResponse(
                error = "Unknown Error",
                message = e.message ?: "",
                httpCode = 0
            )
        )
    }
}