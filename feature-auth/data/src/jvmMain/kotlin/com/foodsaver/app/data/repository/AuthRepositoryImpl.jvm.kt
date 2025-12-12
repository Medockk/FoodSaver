package com.foodsaver.app.data.repository

import com.foodsaver.app.feature.auth.config.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.awt.Desktop
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

actual class GoogleAuthenticator actual constructor() {

    private lateinit var authConnector: AuthConnector
    constructor(authConnector: AuthConnector): this() {
        this.authConnector = authConnector
    }

    internal actual suspend fun getGoogleIdToken(): String? {
        val resultDeferred = CompletableDeferred<String?>()

        val server = embeddedServer(Netty, port = 0) {
            routing {
                get("/callback") {
                    val code = call.parameters["code"]
                    val error = call.parameters["error"]

                    if (code != null) {
                        call.respondText("You successful authenticated. You can close this page and back to app")
                        resultDeferred.complete(code)
                    } else {
                        call.respondText("Authentication error: $error")
                        resultDeferred.complete(null)
                    }
                }
            }
        }.start(wait = false)

        try {
            val port = server.engine.resolvedConnectors().first().port
            val redirect = "http://127.0.0.1:$port/callback"
            val authUrl = authConnector.buildAuthUrl(redirect)

            if (Desktop.isDesktopSupported() && Desktop.getDesktop()
                    .isSupported(Desktop.Action.BROWSE)
            ) {
                Desktop.getDesktop().browse(URI(authUrl))
            } else {
                println("Browser cannot be open")
                return null
            }

            val authCode = resultDeferred.await() ?: return null
            return authConnector.exchangeCodeForGoogleId(authCode, redirect)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            server.stop(1000, 2000)
        }
    }

    class AuthConnector(
        private val httpClient: HttpClient,
    ) {
        private val clientId = BuildConfig.GOOGLE_CLIENT_ID_JVM
        private val clientSecret = BuildConfig.GOOGLE_CLIENT_SECRET_JVM


        fun buildAuthUrl(redirectUri: String): String {
            val scope = "openid email profile"
            val encodedUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
            val encodedScope = URLEncoder.encode(scope, StandardCharsets.UTF_8)

            return "https://accounts.google.com/o/oauth2/v2/auth?" +
                    "client_id=$clientId&" +
                    "redirect_uri=$encodedUri&" +
                    "response_type=code&" +
                    "scope=$encodedScope"
        }

        suspend fun exchangeCodeForGoogleId(code: String, redirectUri: String): String? {
            try {
                val googleAuthResult = httpClient.submitForm(
                    url = "https://oauth2.googleapis.com/token",
                    formParameters = Parameters.build {
                        append("grant_type", "authorization_code")
                        append("client_id", clientId)
                        append("client_secret", clientSecret)
                        append("redirect_uri", redirectUri)
                        append("code", code)
                    }
                ).body<GoogleAuthResponse>()

                return googleAuthResult.idToken
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        @Serializable
        private data class GoogleAuthResponse(
            @SerialName("id_token")
            val idToken: String,
        )
    }

}