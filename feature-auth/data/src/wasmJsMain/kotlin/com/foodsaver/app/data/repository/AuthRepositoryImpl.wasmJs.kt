package com.foodsaver.app.data.repository

actual class GoogleAuthenticator actual constructor() {
    internal actual suspend fun getGoogleIdToken(): String? = null
}