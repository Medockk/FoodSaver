package com.foodsaver.app.utils

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun<reified T> HttpResponse.bodyOrNull(): T? {
    return try {
        this.body()
    } catch (_: Exception) {
        null
    }
}