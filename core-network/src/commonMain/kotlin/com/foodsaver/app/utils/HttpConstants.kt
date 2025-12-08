package com.foodsaver.app.utils

object HttpConstants {

    internal const val ROOT_URL = "http://192.168.0.104:8088"
    const val BASE_URL = "$ROOT_URL/api/"

    internal const val REFRESH_URL = "${BASE_URL}auth/refresh"

    internal const val CSRF_COOKIE_NAME = "XSRF-TOKEN"
    internal const val CSRF_HEADER_NAME = "X-XSRF-TOKEN"

    const val AUTH_URL = "${BASE_URL}auth"
    const val PRODUCTS_URL = "${BASE_URL}products"
    const val CART_URL = "${BASE_URL}cart"
    const val USER_URL = "${BASE_URL}user"
}