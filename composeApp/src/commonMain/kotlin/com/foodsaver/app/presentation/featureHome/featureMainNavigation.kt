@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app.presentation.featureHome

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureCart.CartScreenRoot
import com.foodsaver.app.presentation.featureProductDetail.ProductScreenRoot

context(scope: SharedTransitionScope)
fun NavGraphBuilder.featureHomeNavigation(
    navController: NavController,
    startDestination: Route = Route.MainGraph.HomeScreen
) {

    navigation<Route.MainGraph>(
        startDestination = startDestination
    ) {

        composable<Route.MainGraph.HomeScreen> {
            scope.HomeScreenRoot(navController, this)
        }

        composable<Route.MainGraph.ProductDetailScreen> {
            scope.ProductScreenRoot(
                navController = navController,
                animatedVisibilityScope = this,
            )
        }

        composable<Route.MainGraph.CartScreen> {
            scope.CartScreenRoot(
                navController = navController,
                animatedVisibilityScope = this,
            )
        }
    }
}