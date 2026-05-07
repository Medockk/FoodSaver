package com.foodsaver.app.navigationModule

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {

    @Serializable
    data object OnBoarding: Route()

    @Serializable
    data object AuthGraph : Route() {

        @Serializable
        data object LoginScreen : Route()

        @Serializable
        data object SignupScreen : Route()

        @Serializable
        data object ForgotScreen : Route()

        @Serializable
        data object VerificationScreen : Route()
    }

    @Serializable
    data object HomeGraph : Route() {

        @Serializable
        data object HomeScreen : Route()

        @Serializable
        data class FoodDetailsScreen(
            val productId: String,
            val productName: String,
            val isProductInCart: Boolean,
            val initialQuantity: Long = 1,
        ) : Route()

        @Serializable
        data class Restaurant(
            val restaurantId: String,
            val restaurantName: String,
        ): Route()


    }

    @Serializable
    data object ProfileGraph : Route() {

        @Serializable
        data object ProfileMenuScreen : Route()

        @Serializable
        data object ProfilePersonalInfoScreen : Route()

        @Serializable
        data object ProfileAddressScreen : Route()

        @Serializable
        data object ProfilePaymentMethodScreen : Route()

        @Serializable
        data object ProfileSupportScreen : Route()
    }
}