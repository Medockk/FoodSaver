package com.foodsaver.app.utils

object HttpConstants {

    private const val VERSION = "v1"

    internal const val ROOT_URL = "http://10.195.66.227:8088"
    const val BASE_URL = "$ROOT_URL/api/$VERSION/"

    internal const val REFRESH_URL = "${BASE_URL}refreshToken"

    internal const val CSRF_COOKIE_NAME = "XSRF-TOKEN"
    internal const val CSRF_HEADER_NAME = "X-XSRF-TOKEN"

    const val AUTH_URL = "${BASE_URL}auth"
    const val PRODUCTS_URL = "${BASE_URL}products"
    const val CART_URL = "${BASE_URL}cart"
    const val CATEGORY_URL = "${BASE_URL}category"
    const val USER_URL = "${BASE_URL}user"
    const val OFFER_URL = "${BASE_URL}offers"
    const val BANK_URL = "${BASE_URL}bank"
    const val ADDRESS_URL = "${BASE_URL}address"
    const val ENTERPRISE_URL = "${BASE_URL}restaurant"
    const val FCM_URL = "${BASE_URL}fcm"
    const val INGREDIENTS_URL = "${BASE_URL}ingredients"
    const val PAYMENT_METHOD_URL = "${BASE_URL}paymentMethod"
    const val ORDER_URL = "${BASE_URL}order"
}