package com.foodsaver.app.presentation.featureAdmin.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureAdmin.UpsertCategoryScreenRoot
import com.foodsaver.app.presentation.featureAdmin.UpsertRestaurantAdminScreenRoot
import com.foodsaver.app.presentation.featureUpsertRestaurant.UpsertRestaurantScreenRoot


internal fun NavGraphBuilder.featureAdmin(navController: NavController) {
    navigation<Route.AdminGraph>(
        startDestination = Route.AdminGraph.AdminTabs
    ) {
        composable<Route.AdminGraph.AdminTabs> {
            AdminTabsContainer(navController)
        }

        composable<Route.AdminGraph.UpsertRestaurantScreen> {
            if (it.toRoute<Route.AdminGraph.UpsertRestaurantScreen>().restaurantId == null) {
                UpsertRestaurantScreenRoot(
                    canDeleteRestaurants = false,
                    canEditRestaurants = true,
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            } else {
                UpsertRestaurantScreenRoot(
                    canDeleteRestaurants = true,
                    canEditRestaurants = false,
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
        }

        composable<Route.AdminGraph.UpsertCategoryScreen> {
            UpsertCategoryScreenRoot(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}