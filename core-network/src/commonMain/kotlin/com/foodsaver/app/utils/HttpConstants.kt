package com.foodsaver.app.utils

object HttpConstants {

    internal const val ROOT_URL = "http://192.168.0.101:8088"
    const val BASE_URL = "$ROOT_URL/api/"

    internal const val REFRESH = "auth/refresh"

    internal const val CSRF_COOKIE_NAME = "XSRF-TOKEN"
    internal const val CSRF_HEADER_NAME = "X-XSRF-TOKEN"
}