package com.foodsaver.app.utils.format

actual fun Double.format2(): String {
    return this.asDynamic().toFixed(2)
}