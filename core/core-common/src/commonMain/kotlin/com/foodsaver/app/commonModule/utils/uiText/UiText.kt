package com.foodsaver.app.commonModule.utils.uiText

import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

sealed interface UiText {

    data class DynamicString(val value: String): UiText
    data class StringRes(val resource: StringResource): UiText

    suspend fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringRes -> {
                println("Resources ${resource.key}")
                getString(resource)
            }
        }
    }
}