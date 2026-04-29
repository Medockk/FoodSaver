package com.foodsaver.app.commonModule.apiResult

suspend inline fun <reified T> saveCall(
    crossinline action: suspend () -> T
): T? {
    return try {
        val result = action()
        result
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
suspend inline fun <reified T> saveApiCall(
    crossinline action: suspend () -> T
): ApiResult<T> {
    return try {
        val result = action()
        ApiResult.success(result)
    } catch (e: Exception) {
        e.printStackTrace()
        ApiResult.error(handleError(e))
    }
}