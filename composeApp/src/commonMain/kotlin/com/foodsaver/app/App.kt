@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.foodsaver.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.feature.auth.presentation.Route
import com.foodsaver.app.presentation.SignIn.SignInRoot
import com.foodsaver.app.presentation.SignUp.SignUpRoot

@Composable
fun App() {

    val navController = rememberNavController()

    MaterialExpressiveTheme {
        Scaffold(
            contentWindowInsets = WindowInsets.statusBars
        ) { paddingValues ->
            NavHost(navController, startDestination = Route.AuthGraph) {
                navigation<Route.AuthGraph>(
                    startDestination = Route.AuthGraph.SignInScreen
                ) {
                    composable<Route.AuthGraph.SignInScreen> {
                        SignInRoot(navController)
                    }
                    composable<Route.AuthGraph.SignUpScreen> {
                        SignUpRoot(navController)
                    }
                }

                navigation<Route.MainGraph>(Route.MainGraph.HomeScreen) {
                    composable<Route.MainGraph.HomeScreen> {
                        Box(Modifier.fillMaxSize().background(Color.Green))
                    }
                }
            }
        }
    }
}