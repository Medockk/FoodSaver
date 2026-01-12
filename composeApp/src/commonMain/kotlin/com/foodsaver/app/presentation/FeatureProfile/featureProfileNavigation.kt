package com.foodsaver.app.presentation.FeatureProfile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route

fun NavGraphBuilder.featureProfileNavigation(
    navController: NavController
) {
    navigation<Route.ProfileGraph>(
        startDestination = Route.ProfileGraph.ProfileMenuScreen
    ) {
        composable<Route.ProfileGraph.ProfileMenuScreen> {
            ProfileMenuScreenRoot(
                navController = navController
            )
        }

        composable<Route.ProfileGraph.ProfilePersonalInfoScreen> {
            ProfilePersonalInfoScreenRoot(
                navController = navController
            )
        }

        composable<Route.ProfileGraph.ProfileAddressScreen> {
            ProfileAddressRoot(navController)
        }

        composable<Route.ProfileGraph.ProfilePaymentMethodScreen> {
            ProfilePaymentMethodScreenRoot(navController)
        }

        composable<Route.ProfileGraph.ProfileSupportScreen> {

        }
    }
}