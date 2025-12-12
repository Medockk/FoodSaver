@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app.presentation.FeatureHome

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.foodsaver.app.presentation.FeatureProduct.ProductScreenRoot
import com.foodsaver.app.presentation.MainRoute

context(scope: SharedTransitionScope)
fun NavGraphBuilder.featureHomeNavigation(navController: NavController) {

    composable<MainRoute.HomeScreen> {
        scope.HomeScreenRoot(navController, this)
    }

    composable<MainRoute.ProductScreen> {
        val productId = it.toRoute<MainRoute.ProductScreen>().productId

        scope.ProductScreenRoot(
            productId = productId,
            navController = navController,
            animatedVisibilityScope = this
        )
    }
}