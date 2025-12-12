package com.foodsaver.app.presentation.FeatureAuth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.foodsaver.app.feature.auth.presentation.AuthRoute

fun NavGraphBuilder.featureAuthNavigation(
    navController: NavController,
    onSuccessAuthentication: () -> Unit
) {

    composable<AuthRoute.AuthScreen> {
        AuthScreenRoot(navController, onSuccessAuthentication)
    }

    composable<AuthRoute.ResetPasswordScreen> {
        val token = it.toRoute<AuthRoute.ResetPasswordScreen>().token
    }
}