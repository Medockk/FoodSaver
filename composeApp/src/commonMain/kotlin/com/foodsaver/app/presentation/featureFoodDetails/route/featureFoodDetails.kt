package com.foodsaver.app.presentation.featureFoodDetails.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureFoodDetails.FoodDetailsScreenRoot

fun NavGraphBuilder.featureFoodDetails(navController: NavController) {
    composable<Route.MainGraph.FoodDetailsScreen> {
        FoodDetailsScreenRoot(navController)
    }
}