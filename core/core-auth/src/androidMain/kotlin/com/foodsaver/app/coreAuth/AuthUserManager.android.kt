package com.foodsaver.app.coreAuth

import android.content.Context
import androidx.core.content.edit

actual class AuthUserManager(context: Context) {

    private val sp = context
        .getSharedPreferences("core_auth_prefs", Context.MODE_PRIVATE)

    actual fun getCurrentUid(): String? {
        return sp.getString("uid", null)
    }

    actual fun setCurrentUid(uid: String) {
        sp.edit { remove("uid").putString("uid", uid) }
    }

    actual fun isUserAuthenticated(): Boolean {
        return getCurrentUid() != null
    }

    actual fun logout() {
        sp.edit { clear() }
    }
}