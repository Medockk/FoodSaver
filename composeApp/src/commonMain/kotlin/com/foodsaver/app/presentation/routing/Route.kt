package com.foodsaver.app.presentation.routing

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {

    @Serializable
    data object AuthGraph: Route() {

        @Serializable
        data object AuthScreen: Route()

        @Serializable
        data class ResetPasswordScreen(val token: String): Route()
    }

    @Serializable
    data object MainGraph: Route() {

        @Serializable
        data object HomeScreen: Route()

        @Serializable
        data class ProductDetailScreen(
            val productId: String,
            val isProductInCart: Boolean,
        ): Route()

        @Serializable
        data object CartScreen: Route()
    }

    @Serializable
    data object ProfileGraph: Route() {

        @Serializable
        data object ProfileMenuScreen: Route()

        @Serializable
        data object ProfilePersonalInfoScreen: Route()

        @Serializable
        data object ProfileAddressScreen: Route()

        @Serializable
        data object ProfilePaymentMethodScreen: Route()

        @Serializable
        data object ProfileSupportScreen: Route()
    }
}