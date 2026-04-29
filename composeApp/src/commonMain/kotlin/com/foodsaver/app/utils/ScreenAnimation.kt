package com.foodsaver.app.utils

object ScreenAnimation {

    object Home_ProductDetail {
        fun imageAnim(productId: String) = "home_product_detail_image_$productId"
        fun nameAnim(productId: String) = "home_product_detail_name_$productId"
        fun costAnim(productId: String) = "home_product_detail_cost_$productId"
        fun unitAnim(productId: String) = "home_product_detail_unit_$productId"
        fun expiresAtAnim(productId: String) = "home_product_detail_expires_at_$productId"
        fun buttonAnim(productId: String) = "home_product_detail_button_$productId"
    }

    object Cart_ProductDetail {
        fun imageAnim(productId: String) = "cart_product_detail_image_$productId"
        fun nameAnim(productId: String) = "cart_product_detail_name_$productId"
        fun costAnim(productId: String) = "cart_product_detail_cost_$productId"
        fun countAnim(productId: String) = "cart_product_detail_count_$productId"
        fun unitAnim(productId: String) = "cart_product_detail_unit_$productId"
    }
}