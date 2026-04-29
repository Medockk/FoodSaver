package com.foodsaver.app.feature.auth.presentation.verification

sealed interface VerificationEvent {

    data class OnCodeValueChange(val value: String): VerificationEvent
}