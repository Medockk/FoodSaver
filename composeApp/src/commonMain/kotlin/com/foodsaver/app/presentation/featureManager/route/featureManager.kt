package com.foodsaver.app.presentation.featureManager.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route

internal fun NavGraphBuilder.featureManager(navController: NavController) {

    navigation<Route.ManagerGraph>(
        startDestination = Route.ManagerGraph.MyFoodScreen
    ) {
        composable<Route.ManagerGraph.MyFoodScreen> {

        }

        composable<Route.ManagerGraph.AddProductScreen> {

        }
    }
}