package com.foodsaver.app.domain.model

data class ResetPasswordModel(
    val password: String,
    val confirmPassword: String,
    val resetPasswordToken: String
)
