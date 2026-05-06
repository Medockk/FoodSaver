package com.foodsaver.app.manager

import com.foodsaver.app.utils.HttpConstants

expect class CsrfTokenManager() {

    // TODO delete or realise this manager!

    fun setCsrfToken(token: String, name: String = HttpConstants.CSRF_COOKIE_NAME, path: String = "/")
    fun getCsrfToken(name: String = HttpConstants.CSRF_COOKIE_NAME): String?
}