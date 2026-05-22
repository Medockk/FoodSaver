package com.foodsaver.app.commonModule.utils.uiText

import com.foodsaver.app.commonModule.dto.GlobalErrorResponse
import com.foodsaver.app.core.common.resources.Res
import org.jetbrains.compose.resources.StringResource
import com.foodsaver.app.core.common.resources.*

fun GlobalErrorResponse.asUiText(): UiText {
    println("asUiText globalErrorResponse is $this")
    return when (this.serverErrorCode) {
        // Authentication (1000-1999)
        1002 -> UiText.StringRes(Res.string.error_auth_invalid_email)
        1003 -> UiText.StringRes(Res.string.error_auth_weak_password)
        1004 -> UiText.StringRes(Res.string.error_auth_failed_register)
        1005 -> UiText.StringRes(Res.string.error_auth_failed_authorize)
        1006 -> UiText.StringRes(Res.string.error_auth_jwt_expired)
        1008 -> UiText.StringRes(Res.string.error_auth_refresh_expired)
        1009 -> UiText.StringRes(Res.string.error_auth_password_mismatch)
        1014 -> UiText.StringRes(Res.string.error_auth_google_verify_failed)
        1999 -> UiText.StringRes(Res.string.error_uuid_parse_failed)

        // User (2000-2999)
        2004 -> UiText.StringRes(Res.string.error_user_file_too_large)

        // Product (3000-3999)
        3001 -> UiText.StringRes(Res.string.error_product_not_found)
        3002 -> UiText.StringRes(Res.string.error_product_not_fresh)

        // Cart (4000-4999)
        4001 -> UiText.StringRes(Res.string.error_cart_not_found)

        // Organization (5000-5999)
        5001 -> UiText.StringRes(Res.string.error_org_not_found)

        // Categories (6000-6999)
        6001, 6002 -> UiText.StringRes(Res.string.error_category_not_found)

        // Payment (8000-8999)
        8001 -> UiText.StringRes(Res.string.error_payment_method_not_found)

        // Address (9000-9999)
        9001 -> UiText.StringRes(Res.string.error_address_not_found)

        1001 -> UiText.StringRes(Res.string.error_file_too_large)

        // Security (2000-2999)
        2001 -> UiText.StringRes(Res.string.error_unauthorized_jwt)
        2002 -> UiText.StringRes(Res.string.error_unauthorized_csrf)

        // Authentication & Users (11000-11999)
        11001 -> UiText.StringRes(Res.string.error_user_not_found)
        11002 -> UiText.StringRes(Res.string.error_user_already_registered)
        11003 -> UiText.StringRes(Res.string.error_invalid_google_token)
        11011 -> UiText.StringRes(Res.string.error_jwt_not_expired)
        11012 -> UiText.StringRes(Res.string.error_refresh_token_expired)

        // Product (12000-12999)
        12001 -> UiText.StringRes(Res.string.error_product_not_found)
        12002 -> UiText.StringRes(Res.string.error_product_not_fresh)

        // Profile (13000-13999)
        13001 -> UiText.StringRes(Res.string.error_profile_not_found)

        // Cart (14000-14999)
        14001 -> UiText.StringRes(Res.string.error_cart_not_found)
        14002 -> UiText.StringRes(Res.string.error_cart_item_not_found)

        // Address & Ingredients share code 15001
        15001 -> {
            // Если в GlobalErrorResponse есть поле сообщения или контекста,
            // можно разделить точнее, но пока возвращаем общую ошибку ресурса/адреса
            UiText.StringRes(Res.string.error_address_not_found)
        }

        // Order (20000-20999)
        20001 -> UiText.StringRes(Res.string.error_quantity_out_of_bounds)

        // Default
        else -> UiText.StringRes(Res.string.error_unknown)
    }
}

fun LocalError<*>.asUiText(): UiText {
    println("asUiText localError is ${this.error}")
    return when (val currentMessage = this.error) {
        is String -> UiText.DynamicString(currentMessage)
        is StringResource -> UiText.StringRes(currentMessage)
        else -> UiText.StringRes(Res.string.error_unknown)
    }
}