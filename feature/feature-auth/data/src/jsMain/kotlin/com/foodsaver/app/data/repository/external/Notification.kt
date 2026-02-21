package com.foodsaver.app.data.repository.external

internal external interface Notification {
    fun isNotDisplayed(): Boolean
    fun isSkippedMoment(): Boolean
    fun getNotDisplayedReason(): String
    fun getSkippedReason(): String
}