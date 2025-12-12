package com.foodsaver.app.data.repository.external

internal external object IdConfiguration {
    var client_id: String
    var callback: (CredentialResponse) -> Unit
    var auto_select: Boolean?
    var cancel_on_tap_outside: Boolean?
    var use_fedcm_for_prompt: Boolean?
}