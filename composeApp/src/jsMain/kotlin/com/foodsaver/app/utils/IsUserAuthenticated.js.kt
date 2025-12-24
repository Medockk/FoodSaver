package com.foodsaver.app.utils

import kotlinx.browser.localStorage
import org.w3c.dom.get

actual class IsUserAuthenticated {
    actual operator fun invoke(): Boolean {
        return localStorage["key"] != null
    }

    actual operator fun invoke(value: Boolean) {
        localStorage.setItem("key", value.toString())
    }
}