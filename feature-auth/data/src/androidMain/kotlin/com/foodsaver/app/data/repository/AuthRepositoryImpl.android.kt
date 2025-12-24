package com.foodsaver.app.data.repository

import android.content.Context
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.foodsaver.app.feature.auth.config.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

actual class GoogleAuthenticator {

    private var context: Context
    constructor(context: Context) {
        this.context = context
    }
    internal actual suspend fun getGoogleIdToken(): String? {
        val credentialManager = androidx.credentials.CredentialManager.create(context)

        val option = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID_WEB)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .setCredentialOptions(listOf(option))
            .build()

        val result = credentialManager.getCredential(
            context = context,
            request = request
        )
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken
            return idToken

        }

        return null
    }
}