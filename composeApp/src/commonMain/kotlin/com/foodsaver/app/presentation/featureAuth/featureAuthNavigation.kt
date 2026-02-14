package com.foodsaver.app.presentation.featureAuth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route

fun NavGraphBuilder.featureAuthNavigation(
    navController: NavController,
    onSuccessAuthentication: (uid: String) -> Unit,
    startDestination: Route = Route.AuthGraph.AuthScreen,
) {

    navigation<Route.AuthGraph>(
        startDestination = startDestination
    ) {

        composable<Route.AuthGraph.AuthScreen> {
            AuthScreenRoot(navController, onSuccessAuthentication)
        }

        composable<Route.AuthGraph.ForgotPasswordScreen> {
            ForgotPasswordScreenRoot(navController)
        }

        composable<Route.AuthGraph.ResetPasswordScreen>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "http://0.0.0.0:8087/auth/reset_password/{token}"
                }
            )
        ) {
            ResetPasswordScreenRoot(navController)
        }
    }

}