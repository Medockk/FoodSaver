package com.foodsaver.app.commonModule.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

actual class PlatformContext(val activity: Context) {
    fun getActivity(): Activity? {
        var currentContext = this.activity
        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) return currentContext
            currentContext = currentContext.baseContext
        }

        return null

    }
}