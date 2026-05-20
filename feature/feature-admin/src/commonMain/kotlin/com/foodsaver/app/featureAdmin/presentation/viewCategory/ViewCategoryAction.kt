package com.foodsaver.app.featureAdmin.presentation.viewCategory

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ViewCategoryAction: AppAction {

    data class OnError(val message: String): ViewCategoryAction
}