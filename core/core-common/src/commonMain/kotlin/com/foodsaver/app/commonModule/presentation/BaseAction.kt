package com.foodsaver.app.commonModule.presentation

interface AppAction

sealed interface BaseAction: AppAction {

    data class OnError(val message: String): BaseAction, AppAction
}