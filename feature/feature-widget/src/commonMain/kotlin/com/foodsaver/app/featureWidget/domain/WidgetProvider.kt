package com.foodsaver.app.featureWidget.domain

interface WidgetProvider {

    val isPinSupport: Boolean

    suspend fun requestPinWidget()
}