package com.foodsaver.app.presentation.featureFoodDetails.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureFoodDetails.FoodDetailsScreenRoot

fun NavGraphBuilder.featureFoodDetails(navController: NavController) {
    composable<Route.MainGraph.FoodDetailsScreen>(
        deepLinks = listOf(
            navDeepLink<Route.MainGraph.FoodDetailsScreen>(
                basePath = "foodsaver://app/productDetails"
            ) {
                uriPattern = "foodsaver://app/productDetails/{productId}/{productName}"
            }
        )
    ) {
        FoodDetailsScreenRoot(navController)
    }
}