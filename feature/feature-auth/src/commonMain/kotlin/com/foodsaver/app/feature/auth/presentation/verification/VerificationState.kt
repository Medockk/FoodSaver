package com.foodsaver.app.feature.auth.presentation.verification

data class VerificationState(
    val code: String = "",
    val isLoading: Boolean = false,
    val resendTimerValue: Int = 60,
    val emailToSend: String = ""
)
