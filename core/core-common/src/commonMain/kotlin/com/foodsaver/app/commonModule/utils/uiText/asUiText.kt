package com.foodsaver.app.commonModule.utils.uiText

import com.foodsaver.app.commonModule.dto.GlobalErrorResponse
import com.foodsaver.app.core.common.resources.Res
import com.foodsaver.app.core.common.resources.error_address_not_found
import com.foodsaver.app.core.common.resources.error_auth_failed_authorize
import com.foodsaver.app.core.common.resources.error_auth_failed_register
import com.foodsaver.app.core.common.resources.error_auth_google_verify_failed
import com.foodsaver.app.core.common.resources.error_auth_invalid_email
import com.foodsaver.app.core.common.resources.error_auth_jwt_expired
import com.foodsaver.app.core.common.resources.error_auth_password_mismatch
import com.foodsaver.app.core.common.resources.error_auth_refresh_expired
import com.foodsaver.app.core.common.resources.error_auth_username_occupied
import com.foodsaver.app.core.common.resources.error_auth_weak_password
import com.foodsaver.app.core.common.resources.error_cart_not_found
import com.foodsaver.app.core.common.resources.error_category_not_found
import com.foodsaver.app.core.common.resources.error_org_not_found
import com.foodsaver.app.core.common.resources.error_payment_method_not_found
import com.foodsaver.app.core.common.resources.error_product_not_found
import com.foodsaver.app.core.common.resources.error_product_not_fresh
import com.foodsaver.app.core.common.resources.error_unknown
import com.foodsaver.app.core.common.resources.error_user_empty_email
import com.foodsaver.app.core.common.resources.error_user_file_too_large
import com.foodsaver.app.core.common.resources.error_user_not_found
import com.foodsaver.app.core.common.resources.error_uuid_parse_failed
import org.jetbrains.compose.resources.StringResource

fun GlobalErrorResponse.asUiText(): UiText {
    println("asUiText globalErrorResponse is $this")
    return when (this.serverErrorCode) {
        // Authentication (1000-1999)
        1001 -> UiText.StringRes(Res.string.error_auth_username_occupied)
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
        2001 -> UiText.StringRes(Res.string.error_user_not_found)
        2002 -> UiText.StringRes(Res.string.error_user_empty_email)
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