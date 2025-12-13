package com.foodsaver.app.presentation.Home

sealed interface HomeAction {

    data class OnError(val message: String): HomeAction
}