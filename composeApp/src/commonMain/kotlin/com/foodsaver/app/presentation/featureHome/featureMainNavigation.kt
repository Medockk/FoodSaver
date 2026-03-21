@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app.presentation.featureHome

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureAddProduct.AddProductScreenRoot
import com.foodsaver.app.presentation.featureCart.CartScreenRoot
import com.foodsaver.app.presentation.featureEnterprise.EnterpriseScreenRoot
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

        composable<Route.MainGraph.ProductDetailScreen>(
            deepLinks = listOf(
                navDeepLink<Route.MainGraph.ProductDetailScreen>(
                    basePath = "foodsaver://app/productDetails"
                ) {
                    uriPattern = "foodsaver://app/productDetails/{productId}/{isProductInCart}"
                }
            ),
            enterTransition = { fadeIn(tween()) + scaleIn(initialScale = 0.95f) },
            exitTransition = { fadeOut(tween(150)) }
        ) {
            scope.ProductScreenRoot(
                navController = navController,
                animatedVisibilityScope = this,
            )
        }

        composable<Route.MainGraph.MapScreen> {
            EnterpriseScreenRoot(navController)
        }

        composable<Route.MainGraph.CartScreen> {
            scope.CartScreenRoot(
                navController = navController,
                animatedVisibilityScope = this,
            )
        }

        composable<Route.MainGraph.AddProductScreen> {
            AddProductScreenRoot(navController)
        }
    }
}