package com.foodsaver.app.domain.utils

import com.foodsaver.app.commonModule.utils.uiText.LocalError

sealed class AuthExceptions: Exception(), LocalError<Any> {

    override val error: Any = this

    class NoGoogleAccount: AuthExceptions()
    class FailedToExactActivityFromContext: AuthExceptions()

    class InvalidEmail: AuthExceptions()
    class PasswordNotEquals: AuthExceptions()
}