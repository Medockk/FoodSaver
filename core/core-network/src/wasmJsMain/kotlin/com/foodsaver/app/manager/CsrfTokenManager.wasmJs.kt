package com.foodsaver.app.manager

import kotlinx.browser.document

actual class CsrfTokenManager actual constructor() {
    actual fun setCsrfToken(token: String, name: String, path: String) {
        document.cookie = "$name=$token; path=$path; SameSite=Lax"
    }

    actual fun getCsrfToken(name: String): String? {
        return null
    }
}