package com.foodsaver.app.utils.format

import java.util.Locale

actual fun Double.format2(): String {
    return "%.2f".format(Locale.getDefault(), this)
}