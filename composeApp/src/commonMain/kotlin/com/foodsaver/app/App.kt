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
import com.foodsaver.app.feature.auth.presentation.AuthRoute
import com.foodsaver.app.presentation.FeatureAuth.featureAuthNavigation
import com.foodsaver.app.presentation.FeatureHome.featureHomeNavigation
import com.foodsaver.app.presentation.MainRoute
import com.foodsaver.app.ui.colorScheme

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    initialRoute: AuthRoute = AuthRoute.AuthScreen,
) {
    MaterialTheme(
        colorScheme = colorScheme()
    ) {
        SharedTransitionLayout {
            Scaffold(
                contentWindowInsets = WindowInsets.statusBars
            ) { _ ->
                NavHost(navController, startDestination = MainRoute.HomeScreen/*initialRoute*/) {
                    featureAuthNavigation(navController, onSuccessAuthentication = {
                        navController.navigate(MainRoute.HomeScreen)
                    })

                    featureHomeNavigation(navController)
                }
            }
        }
    }
}