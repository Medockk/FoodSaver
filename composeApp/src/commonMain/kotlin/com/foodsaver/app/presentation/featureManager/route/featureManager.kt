package com.foodsaver.app.presentation.featureManager.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureManager.AddProductScreenRoot

internal fun NavGraphBuilder.featureManager(navController: NavController) {

    navigation<Route.ManagerGraph>(
        startDestination = Route.ManagerGraph.TabsContainer()
    ) {

        composable<Route.ManagerGraph.TabsContainer> {
            ManagerTabsContainer(navController)
        }

        composable<Route.ManagerGraph.AddProductScreen> {
            AddProductScreenRoot(navController)
        }
    }
}