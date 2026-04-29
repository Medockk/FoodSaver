package com.foodsaver.app.utils

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.dto.GlobalErrorResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

suspend inline fun<reified T> saveNetworkCall(
    crossinline action: suspend () -> HttpResponse
) : ApiResult<T> {
    return try {
        val result = action.invoke()

        // if status code between 200..299
        if (result.status.isSuccess()) {

            // if no content return null
            if (result.status == HttpStatusCode.NoContent) {
                return ApiResult.success(null as T)
            }

            val body = result.body<T>()
            println("SaveNetworkCall: Body: $body")
            ApiResult.success(body)
        } else {
            val errorBodyText = result.bodyAsText()

            val errorResult = runCatching {
                val decodedBody = Json.decodeFromString<GlobalErrorResponse>(errorBodyText)
                return@runCatching decodedBody
            }.getOrElse {
                it.printStackTrace()
                return ApiResult.error(
                    error = GlobalErrorResponse(
                        error = "Server error: ${result.status.value}",
                        message = "Unknown error",
                        httpCode = 0
                    )
                )
            }

            ApiResult.error(errorResult)
        }
    } catch (e: Exception) {
        e.printStackTrace()

        val message = if (e.message?.startsWith("Failed to connect to", ignoreCase = true) == true) {
            "Server is not responding. Check your internet connection"
        } else {
            "Oops... Unknown error"
        }

        ApiResult.error(
            GlobalErrorResponse(
                error = e::class.simpleName ?: "Unknown error",
                message = message,
                httpCode = 0
            )
        )
    }
}