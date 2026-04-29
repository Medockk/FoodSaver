package com.foodsaver.app.presentation.featureOnBoarding.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureOnBoarding.OnBoardingScreenRoot

fun NavGraphBuilder.featureOnboarding(navController: NavController, onOnboardingComplete: () -> Unit) {
    composable<Route.OnBoarding> {
        OnBoardingScreenRoot(navController, onOnboardingComplete)
    }
}