package com.foodsaver.app.featureOrder.presentation

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface OrderAction: AppAction {

    data class OnError(val message: String): OrderAction
}