package com.foodsaver.app.data.repository

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.foodsaver.app.commonModule.utils.PlatformContext
import com.foodsaver.app.domain.utils.AuthExceptions
import com.foodsaver.app.feature.auth.config.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

actual class GoogleAuthenticator {

    private var context: Context
    constructor(context: Context) {
        this.context = context
    }
    internal actual suspend fun getGoogleIdToken(platformContext: PlatformContext): String? {
        val credentialManager = androidx.credentials.CredentialManager.create(context)

        val option = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID_WEB)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .setCredentialOptions(listOf(option))
            .build()

        val result = try {
            val activity = platformContext.activity
                .findActivity() ?: throw AuthExceptions.FailedToExactActivityFromContext()
            credentialManager.getCredential(
                context = activity,
                request = request
            )
        } catch (e: GetCredentialException) {
            e.printStackTrace()
            when (e) {
                is NoCredentialException -> {
                    throw AuthExceptions.NoGoogleAccount()
                }
            }
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            println("FEATURE-AUTH: IF SUCCESS!")
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken
            return idToken

        }

        return null
    }

    private fun Context.findActivity(): Activity? {
        var currentContext = this
        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) return currentContext
            currentContext = currentContext.baseContext
        }

        return null
    }
}