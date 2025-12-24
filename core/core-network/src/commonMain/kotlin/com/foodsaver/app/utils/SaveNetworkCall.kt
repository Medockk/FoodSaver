package com.foodsaver.app.utils

import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.dto.GlobalErrorResponse
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
                val decodedBody = Json.decodeFromString<GlobalErrorResponse>(errorBodyText)
                return@runCatching decodedBody
            }.getOrElse {
                it.printStackTrace()
                return ApiResult.Error(
                    error = GlobalErrorResponse(
                        error = "Server error: ${result.status.value}",
                        message = "Unknown error",
                        httpCode = 0
                    )
                )
            }

            ApiResult.Error(errorResult)
        }
    } catch (e: Exception) {
        e.printStackTrace()

        val message = if (e.message?.startsWith("Failed to connect to", ignoreCase = true) == true) {
            "Server is not responding. Check your internet connection"
        } else {
            "Oops... Unknown error"
        }

        ApiResult.Error(
            GlobalErrorResponse(
                error = e.message ?: "Unknown",
                message = message,
                httpCode = 0
            )
        )
    }
}