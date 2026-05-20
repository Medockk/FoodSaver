package com.foodsaver.app.presentation.featureAdmin.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureUpsertRestaurant.UpsertRestaurantScreenRoot


internal fun NavGraphBuilder.featureAdmin(navController: NavController) {
    navigation<Route.AdminGraph>(
        startDestination = Route.AdminGraph.AdminTabs
    ) {
        composable<Route.AdminGraph.AdminTabs> {
            AdminTabsContainer(navController)
        }

        composable<Route.AdminGraph.UpsertRestaurantScreen> {
            UpsertRestaurantScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable<Route.AdminGraph.UpsertCategoryScreen> {

        }
    }
}