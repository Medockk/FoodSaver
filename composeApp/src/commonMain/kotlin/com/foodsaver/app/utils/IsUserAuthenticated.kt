package com.foodsaver.app.utils

expect class IsUserAuthenticated {

    operator fun invoke(): Boolean
    operator fun invoke(value: Boolean)
}