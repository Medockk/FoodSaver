package com.foodsaver.app.presentation.featureCart.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureCart.CartScreenRoot

internal fun NavGraphBuilder.featureCart(navController: NavController) {

    navigation<Route.CartGraph>(
        startDestination = Route.CartGraph.CartScreen()
    ) {

        composable<Route.CartGraph.CartScreen> {
            CartScreenRoot(navController)
        }
    }
}