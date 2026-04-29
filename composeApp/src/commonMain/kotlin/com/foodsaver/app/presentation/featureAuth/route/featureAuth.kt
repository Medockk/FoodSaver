package com.foodsaver.app.presentation.featureAuth.route

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureAuth.forgotPassword.ForgotPasswordScreenRoot
import com.foodsaver.app.presentation.featureAuth.login.LoginScreenRoot
import com.foodsaver.app.presentation.featureAuth.signup.SignupScreenRoot
import com.foodsaver.app.presentation.featureAuth.verification.VerificationScreenRoot

fun NavGraphBuilder.featureAuth(
    navController: NavController,
    onLogged: (String?) -> Unit
) {
    navigation<Route.AuthGraph>(
        startDestination = Route.AuthGraph.LoginScreen
    ) {

        val enterTransition = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        val popEnterTransition = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        val popExitTransition = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = spring(
                stiffness = Spring.StiffnessMedium
            )
        )

        composable<Route.AuthGraph.LoginScreen>(
            enterTransition = {
                enterTransition
            },
            popEnterTransition = {
                popEnterTransition
            },
        ) {
            LoginScreenRoot(navController, onLogged = onLogged)
        }
        composable<Route.AuthGraph.SignupScreen>(
            enterTransition = {
                enterTransition
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            },
            popEnterTransition = {
                popEnterTransition
            },
            popExitTransition = {
                popExitTransition
            }
        ) {
            SignupScreenRoot(navController, onLogged = onLogged)
        }
        composable<Route.AuthGraph.ForgotScreen>(
            enterTransition = { enterTransition },
            popEnterTransition =  { popEnterTransition },
            popExitTransition =  { popExitTransition }
        ) {
            ForgotPasswordScreenRoot(navController)
        }
        composable<Route.AuthGraph.VerificationScreen> {
            VerificationScreenRoot(navController)
        }
    }
}