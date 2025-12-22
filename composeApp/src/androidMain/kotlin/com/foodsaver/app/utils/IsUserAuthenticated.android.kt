package com.foodsaver.app.utils

import android.content.Context
import androidx.core.content.edit

actual class IsUserAuthenticated(context: Context) {

    private val sp = context.getSharedPreferences("is_authenticate", Context.MODE_PRIVATE)
    actual operator fun invoke(): Boolean {
        return sp.getBoolean("key", false)
    }

    actual operator fun invoke(value: Boolean) {
        sp.edit { putBoolean("key", value) }
    }
}