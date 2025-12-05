package com.foodsaver.app.feature.auth.data.external

internal external interface CredentialResponse {
    val credential: String
    val select_by: String?
}