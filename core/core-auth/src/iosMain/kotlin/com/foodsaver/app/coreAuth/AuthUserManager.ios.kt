package com.foodsaver.app.coreAuth

import platform.Foundation.NSUserDefaults

actual class AuthUserManager {

    private val storage = NSUserDefaults.standardUserDefaults

    actual fun getCurrentUid(): String? {
        return storage.stringForKey("uid")
    }

    actual fun setCurrentUid(uid: String) {
        storage.setObject("uid", uid)
    }

    actual fun isUserAuthenticated(): Boolean {
        return getCurrentUid() != null
    }

    actual fun logout() {
        storage.removeObjectForKey("uid")
    }
}