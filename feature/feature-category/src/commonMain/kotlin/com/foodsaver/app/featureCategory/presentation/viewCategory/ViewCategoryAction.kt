package com.foodsaver.app.featureCategory.presentation.viewCategory

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ViewCategoryAction: AppAction {

    data class OnError(val message: String): ViewCategoryAction
}