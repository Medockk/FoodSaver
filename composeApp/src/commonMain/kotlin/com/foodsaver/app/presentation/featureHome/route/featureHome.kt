package com.foodsaver.app.presentation.featureHome.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureFoodDetails.route.featureFoodDetails
import com.foodsaver.app.presentation.featureHome.HomeScreenRoot
import com.foodsaver.app.presentation.featureRestaurant.route.featureRestaurant
import com.foodsaver.app.presentation.featureSearch.route.featureSearch

fun NavGraphBuilder.featureHome(navController: NavController) {
    navigation<Route.MainGraph>(
        startDestination = Route.MainGraph.HomeScreen
    ) {
        composable<Route.MainGraph.HomeScreen> {
            HomeScreenRoot(navController)
        }

        featureRestaurant(navController)

        featureFoodDetails(navController)

        featureSearch(navController)
    }
}