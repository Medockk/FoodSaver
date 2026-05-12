package com.foodsaver.app.presentation.featureRestaurant.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureRestaurant.RestaurantScreenRoot

fun NavGraphBuilder.featureRestaurant(navController: NavController) {
    composable<Route.MainGraph.Restaurant>() {
        RestaurantScreenRoot(navController)
    }
}