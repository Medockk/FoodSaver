package com.foodsaver.app.domain.utils

sealed class AuthExceptions(): Exception() {

    class NoGoogleAccount: AuthExceptions()
    class FailedToExactActivityFromContext: AuthExceptions()
}