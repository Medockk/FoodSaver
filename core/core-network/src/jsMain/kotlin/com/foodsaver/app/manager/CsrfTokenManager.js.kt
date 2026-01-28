package com.foodsaver.app.manager

import kotlinx.browser.document

actual class CsrfTokenManager actual constructor() {

    actual fun setCsrfToken(token: String, name: String, path: String) {
        document.cookie = "$name=$token; path=$path; SameSite=Lax"
        println("\n\n\nCookie for $name is $token\n\n\n")
    }

    actual fun getCsrfToken(name: String): String? {
        val cookies = document.cookie.split("; ")
        val match = cookies.firstOrNull { it.startsWith("$name=") }
        return match?.substringAfterLast("=")
    }
}