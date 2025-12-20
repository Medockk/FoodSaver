@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.presentation.FeatureAuth.featureAuthNavigation
import com.foodsaver.app.presentation.FeatureHome.featureHomeNavigation
import com.foodsaver.app.presentation.FeatureProfile.featureProfileNavigation
import com.foodsaver.app.presentation.routing.Route
import com.foodsaver.app.ui.colorScheme

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    initialAuthRoute: Route = Route.AuthGraph.AuthScreen,
) {
    MaterialTheme(
        colorScheme = colorScheme()
    ) {
        SharedTransitionLayout {
            Scaffold(
                contentWindowInsets = WindowInsets.statusBars
            ) { _ ->
                NavHost(navController, startDestination = Route.MainGraph) {
                    featureAuthNavigation(
                        navController = navController,
                        startDestination = initialAuthRoute,
                        onSuccessAuthentication = {
                            navController.navigate(Route.MainGraph.HomeScreen)
                        })

                    featureHomeNavigation(navController)
                    featureProfileNavigation(navController)
                }
            }
        }
    }
}