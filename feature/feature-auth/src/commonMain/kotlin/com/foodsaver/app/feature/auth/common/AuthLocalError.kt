package com.foodsaver.app.feature.auth.common

import com.foodsaver.app.commonModule.utils.uiText.LocalError
import com.foodsaver.app.core.common.resources.error_unknown
import com.foodsaver.app.domain.utils.AuthExceptions
import foodsaver.feature.feature_auth.generated.resources.Res
import foodsaver.feature.feature_auth.generated.resources.local_error_empty_email
import foodsaver.feature.feature_auth.generated.resources.local_error_empty_fio
import foodsaver.feature.feature_auth.generated.resources.local_error_empty_password
import foodsaver.feature.feature_auth.generated.resources.local_error_invalid_email
import foodsaver.feature.feature_auth.generated.resources.local_error_no_google_account
import foodsaver.feature.feature_auth.generated.resources.local_error_passwords_not_equal
import org.jetbrains.compose.resources.StringResource

sealed interface AuthLocalError: LocalError<StringResource> {

    data object InvalidEmail: AuthLocalError {
        override val error: StringResource = Res.string.local_error_invalid_email
    }

    data object NoGoogleAccount: AuthLocalError {
        override val error: StringResource = Res.string.local_error_no_google_account
    }

    data object PasswordNotEqual: AuthLocalError {
        override val error: StringResource = Res.string.local_error_passwords_not_equal
    }

    data object UnknownError: AuthLocalError {
        override val error: StringResource = com.foodsaver.app.core.common.resources.Res.string.error_unknown
    }

    data object EmptyFio: AuthLocalError {
        override val error: StringResource = Res.string.local_error_empty_fio
    }
    data object EmptyEmail: AuthLocalError {
        override val error: StringResource = Res.string.local_error_empty_email
    }
    data object EmptyPassword: AuthLocalError {
        override val error: StringResource = Res.string.local_error_empty_password
    }

    companion object {
        internal fun AuthExceptions.fromException(): AuthLocalError {
            return when (this) {
                is AuthExceptions.InvalidEmail -> InvalidEmail
                is AuthExceptions.NoGoogleAccount -> NoGoogleAccount
                is AuthExceptions.PasswordNotEquals -> PasswordNotEqual
                else -> UnknownError
            }
        }
    }
}