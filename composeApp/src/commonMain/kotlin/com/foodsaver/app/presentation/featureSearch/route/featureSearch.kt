package com.foodsaver.app.presentation.featureSearch.route

import androidx.compose.animation.slideInVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureSearch.SearchScreenRoot

internal fun NavGraphBuilder.featureSearch(navController: NavController) {
    composable<Route.MainGraph.SearchScreen>(
        enterTransition = {
            slideInVertically()
        },
        popEnterTransition = {
            slideInVertically()
        },
    ) {
        SearchScreenRoot(navController)
    }
}