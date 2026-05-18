package com.foodsaver.app.presentation.featureProfile.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureProfile.AddNewAddressScreenRoot
import com.foodsaver.app.presentation.featureProfile.EditProfileScreenRoot
import com.foodsaver.app.presentation.featureProfile.ProfileAddressScreenRoot
import com.foodsaver.app.presentation.featureProfile.ProfileMenuScreenRoot
import com.foodsaver.app.presentation.featureProfile.ProfilePersonalInfoScreenRoot

internal fun NavGraphBuilder.featureProfile(navController: NavController) {

    navigation<Route.ProfileGraph>(
        startDestination = Route.ProfileGraph.ProfileMenuScreen
    ) {
        composable<Route.ProfileGraph.ProfileMenuScreen> {
            ProfileMenuScreenRoot(navController)
        }
        composable<Route.ProfileGraph.ProfilePersonalInfoScreen> {
            ProfilePersonalInfoScreenRoot(navController)
        }
        composable<Route.ProfileGraph.EditProfileScreen> {
            EditProfileScreenRoot(navController)
        }
        composable<Route.ProfileGraph.ProfileAddressScreen> {
            ProfileAddressScreenRoot(navController)
        }
        composable<Route.ProfileGraph.AddNewAddressScreen> {
            AddNewAddressScreenRoot(navController)
        }
    }
}