package com.foodsaver.app.featureMyFood.presentation

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface MyFoodAction: AppAction {

    data class OnError(val message: String): MyFoodAction
    data object OnSuccess: MyFoodAction
}