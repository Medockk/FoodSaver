package com.foodsaver.app.addProductModule.presentation.addProduct

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface AddProductAction: AppAction {

    data class OnError(val message: String): AddProductAction
    data object OnSuccessUpsert: AddProductAction
}