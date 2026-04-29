package com.foodsaver.app.coreAuth

import kotlinx.browser.localStorage

actual class AuthUserManager {

    actual fun getCurrentUid(): String? {
        return localStorage.getItem("uid")
    }

    actual fun setCurrentUid(uid: String) {
        localStorage.setItem("uid", uid)
    }

    actual fun isUserAuthenticated(): Boolean {
        return getCurrentUid() != null
    }

    actual fun logout() {
        localStorage.removeItem("uid")
    }
}