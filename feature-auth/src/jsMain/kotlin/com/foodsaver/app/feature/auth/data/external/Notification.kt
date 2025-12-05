package com.foodsaver.app.feature.auth.data.external

internal external interface Notification {
    fun isNotDisplayed(): Boolean
    fun isSkippedMoment(): Boolean
    fun getNotDisplayedReason(): String
    fun getSkippedReason(): String
}