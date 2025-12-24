package com.foodsaver.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.di.initSharedKoin
import com.foodsaver.app.di.uiModule
import com.foodsaver.app.presentation.routing.Route
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initSharedKoin(arrayOf(uiModule))
    ComposeViewport {
        val route = getRoute()
        val navController = rememberNavController()
        App(navController, initialAuthRoute = route)
    }
}

private fun getRoute(): Route {
    val path = window.location.pathname.removePrefix("/")
    println("Path is $path")
    val query = window.location.search.removePrefix("?")
    println("Query is $query")

    val params = query.split("&").mapNotNull {
        val (key, value) = it.split("=").let { let -> let[0] to let.getOrNull(1) }

        if (value != null) key to value
        else null
    }.toMap()
    println("Params is $params")

    return when(path) {
        "reset-password" -> Route.AuthGraph.ResetPasswordScreen(params["id"].orEmpty())
        else -> Route.AuthGraph.AuthScreen
    }
}