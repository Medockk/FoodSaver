package com.foodsaver.app.utils.format

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

actual fun Double.format2(): String {
    val formatter = NSNumberFormatter().apply {
        minimumFractionDigits = 2u
        maximumFractionDigits = 2u
    }
    return formatter.stringFromNumber(NSNumber(double = this)) ?: this.toString()
}