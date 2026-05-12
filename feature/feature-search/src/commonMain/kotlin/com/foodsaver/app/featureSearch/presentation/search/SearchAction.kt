package com.foodsaver.app.featureSearch.presentation.search

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface SearchAction: AppAction {

    data class OnError(val message: String): SearchAction
}