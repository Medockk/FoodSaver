@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.foodsaver.app

import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(viewModel: AppViewModel = koinViewModel()) {
    val navController = rememberNavController()
    MaterialExpressiveTheme(
        motionScheme = MotionScheme.expressive()
    ) {
        val animationSpec = MaterialTheme.motionScheme.slowSpatialSpec<IntSize>()
        NavHost(navController, startDestination = "1") {
            composable(
                route = "1",
                enterTransition = {
                    expandHorizontally(animationSpec)
                },
            ) {
                App1(navController)
            }
            composable(
                route = "2",
                enterTransition = {
                    expandHorizontally(animationSpec)
                },
            ) {
                App2(navController)
            }
        }
    }
}

@Composable
fun App1(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                navController.navigate("2")
            }
        ) {
            Text(
                text = "to 2"
            )
        }
    }
}

@Composable
fun App2(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                navController.navigate("1")
            }
        ) {
            Text(
                text = "to 1"
            )
        }
    }
}