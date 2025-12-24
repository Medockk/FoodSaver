package com.foodsaver.app.data.repository

import com.foodsaver.app.data.repository.external.IdConfiguration
import com.foodsaver.app.data.repository.external.google
import com.foodsaver.app.feature.auth.config.BuildConfig
import kotlinx.browser.window
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class GoogleAuthenticator {
    private val clientId: String = BuildConfig.GOOGLE_CLIENT_ID_WEB
    internal actual suspend fun getGoogleIdToken(): String? = suspendCoroutine {
        val config = js("{}").unsafeCast<IdConfiguration>()
        config.client_id = clientId
        config.auto_select = false
        config.use_fedcm_for_prompt = false
        config.cancel_on_tap_outside = true
        println(config.client_id)
        println("Origin is: " + window.location.origin)

        config.callback = { response ->
            println("Callback response: $response")
            it.resume(response.credential)
        }

        try {
            google.accounts.id.initialize(config)
            println("Successful initialize")
            google.accounts.id.prompt { notification ->
                if (notification.isNotDisplayed() || notification.isSkippedMoment()) {
                    println("Google Auth skipped or not displayed")
                    println("Not displayed reason is: ${notification.getNotDisplayedReason()}")
                    println("Skipped reason is: ${notification.getSkippedReason()}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            it.resume(null)
        }
    }
}