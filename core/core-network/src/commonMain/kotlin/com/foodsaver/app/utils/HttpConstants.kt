package com.foodsaver.app.utils

object HttpConstants {

    private const val VERSION = "v1"

    internal const val ROOT_URL = "http://10.44.66.227:8088"
    const val BASE_URL = "$ROOT_URL/api/$VERSION/"

    internal const val REFRESH_URL = "${BASE_URL}auth/refresh"

    internal const val CSRF_COOKIE_NAME = "XSRF-TOKEN"
    internal const val CSRF_HEADER_NAME = "X-XSRF-TOKEN"

    const val AUTH_URL = "${BASE_URL}auth"
    const val PRODUCTS_URL = "${BASE_URL}products"
    const val CART_URL = "${BASE_URL}cart"
    const val USER_URL = "${BASE_URL}user"
    const val OFFER_URL = "${BASE_URL}offers"
    const val BANK_URL = "${BASE_URL}bank"
    const val ADDRESS_URL = "${BASE_URL}address"
}