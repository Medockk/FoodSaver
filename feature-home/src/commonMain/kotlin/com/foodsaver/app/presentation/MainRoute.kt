package com.foodsaver.app.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoute {

    @Serializable
    data object HomeScreen: MainRoute()

    @Serializable
    data class ProductScreen(val productId: String): MainRoute()
}