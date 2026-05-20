package com.foodsaver.app.featureCategory.presentation.upsertCategoryViewModel

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface UpsertCategoryAction: AppAction {

    data class OnError(val message: String): UpsertCategoryAction
    data object OnCategoryUpserted: UpsertCategoryAction
}