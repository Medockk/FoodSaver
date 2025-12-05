package com.foodsaver.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.foodsaver.app.feature.auth.presentation.Route
import com.foodsaver.app.presentation.Auth.AuthScreenRoot
import com.foodsaver.app.ui.colorScheme

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    initialRoute: Route = Route.AuthGraph,
) {
    MaterialTheme(
        colorScheme = colorScheme()
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.statusBars
        ) { _ ->
            NavHost(navController, startDestination = initialRoute) {
                navigation<Route.AuthGraph>(
                    startDestination = Route.AuthGraph.AuthScreen
                ) {
                    composable<Route.AuthGraph.AuthScreen> {
                        AuthScreenRoot(navController)
                    }
                }

                composable<Route.ResetGraph.ResetPassword> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(it.toRoute<Route.ResetGraph.ResetPassword>().token)
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