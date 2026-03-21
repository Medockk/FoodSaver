package com.foodsaver.app.manager

actual class CsrfTokenManager actual constructor() {
    actual fun setCsrfToken(token: String, name: String, path: String) {
    }

    actual fun getCsrfToken(name: String): String? {
        return null
    }
}