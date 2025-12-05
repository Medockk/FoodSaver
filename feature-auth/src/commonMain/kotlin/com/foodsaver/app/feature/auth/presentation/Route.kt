package com.foodsaver.app.feature.auth.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {

    @Serializable
    data object AuthGraph: Route() {

        @Serializable
        data object AuthScreen: Route()
    }

    @Serializable
    data object ResetGraph: Route() {

        @Serializable
        data class ResetPassword(val token: String): Route()
    }

    @Serializable
    data object MainGraph: Route() {

        @Serializable
        data object HomeScreen: Route()
    }
}