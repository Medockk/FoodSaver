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
    data object MainGraph : Route() {

        @Serializable
        data object HomeScreen : Route()

        @Serializable
        data class FoodDetailsScreen(
            val productId: String,
            val productName: String,
            var productCartItemId: String? = null,
            val initialQuantity: Long = 1,
        ) : Route()

        @Serializable
        data class Restaurant(
            val restaurantId: String,
            val restaurantName: String,
        ): Route()

        @Serializable
        data class SearchScreen(
            val searchCategoryId: String? = null,
            val categoryName: String? = null,
            val query: String? = null
        ): Route()


    }

    @Serializable
    data object CartGraph: Route() {

        @Serializable
        data class CartScreen(
            val cartId: String? = null,
            val cartPrice: Double = 0.0
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
        data object EditProfileScreen : Route()

        @Serializable
        data object AddNewAddressScreen : Route()
    }

    @Serializable
    data object PaymentMethodGraph: Route() {

        @Serializable
        data class PaymentMethodScreen(
            val price: Double,
            val currency: String = ""
        ): Route()

        @Serializable
        data class AddCardScreen(
            val typeId: String = ""
        ): Route()

        @Serializable
        data object PaymentSuccessfulScreen: Route()
    }

    @Serializable
    data object OrderGraph: Route() {

        @Serializable
        data object OrderScreen: Route()

        @Serializable
        data class TrackingScreen(val orderId: String): Route()
    }
}