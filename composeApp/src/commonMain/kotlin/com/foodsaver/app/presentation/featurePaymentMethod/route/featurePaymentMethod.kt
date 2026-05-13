package com.foodsaver.app.presentation.featurePaymentMethod.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featurePaymentMethod.PaymentMethodScreenRoot
import com.foodsaver.app.presentation.featurePaymentMethod.AddCardScreenRoot

internal fun NavGraphBuilder.featurePaymentMethod(navController: NavController) {
    navigation<Route.PaymentMethodGraph>(
        startDestination = Route.PaymentMethodGraph.PaymentMethodScreen(0.0)
    ) {
        composable<Route.PaymentMethodGraph.PaymentMethodScreen> {
            PaymentMethodScreenRoot(navController)
        }

        composable<Route.PaymentMethodGraph.AddCardScreen> {
            AddCardScreenRoot(navController)
        }
    }
}