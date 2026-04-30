package com.foodsaver.app.presentation.featureHome.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureFoodDetails.route.featureFoodDetails
import com.foodsaver.app.presentation.featureHome.HomeScreenRoot
import com.foodsaver.app.presentation.featureRestaurant.route.featureRestaurant

fun NavGraphBuilder.featureHome(navController: NavController) {
    navigation<Route.HomeGraph>(
        startDestination = Route.HomeGraph.HomeScreen
    ) {
        composable<Route.HomeGraph.HomeScreen> {
            HomeScreenRoot(navController)
        }

        featureRestaurant(navController)

        featureFoodDetails(navController)
    }
}