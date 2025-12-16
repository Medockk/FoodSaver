package com.foodsaver.app.presentation.FeatureAuth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.foodsaver.app.presentation.routing.Route

fun NavGraphBuilder.featureAuthNavigation(
    navController: NavController,
    onSuccessAuthentication: () -> Unit,
    startDestination: Route = Route.AuthGraph.AuthScreen,
) {

    navigation<Route.AuthGraph>(
        startDestination = startDestination
    ) {

        composable<Route.AuthGraph.AuthScreen> {
            AuthScreenRoot(navController, onSuccessAuthentication)
        }

        composable<Route.AuthGraph.ResetPasswordScreen>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "http://localhost:8087/auth/reset_password/{token}"
                }
            )
        ) {
            val token = it.toRoute<Route.AuthGraph.ResetPasswordScreen>().token
        }
    }

}