package com.foodsaver.app.presentation.featureOrder.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureOrder.OrderScreenRoot

fun NavGraphBuilder.featureOrder(navController: NavController) {

    navigation<Route.OrderGraph>(
        startDestination = Route.OrderGraph.OrderScreen
    ) {
        composable<Route.OrderGraph.OrderScreen>(
            deepLinks = listOf(
                navDeepLink<Route.OrderGraph.OrderScreen>(
                    basePath = "foodsaver://app/order"
                ) {
                    uriPattern = "foodsaver://app/order"
                }
            )
        ) {
            OrderScreenRoot(navController)
        }

        composable<Route.OrderGraph.TrackingScreen> {

        }
    }
}