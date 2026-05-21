package com.foodsaver.app.presentation.featureUpsertRestaurant.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureUpsertRestaurant.UpsertRestaurantScreenRoot

internal fun NavGraphBuilder.featureUpsertRestaurant(navController: NavController) {
    navigation<Route.UpsertRestaurantGraph>(
        startDestination = Route.UpsertRestaurantGraph.UpsertRestaurantScreen()
    ) {
        composable<Route.UpsertRestaurantGraph.UpsertRestaurantScreen> {
            UpsertRestaurantScreenRoot(
                canEditRestaurants = true,
                canDeleteRestaurants = false,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}