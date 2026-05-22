@file:OptIn(ExperimentalForeignApi::class)

package com.foodsaver.app.data.repository

import cocoapods.GoogleSignIn.GIDSignIn
import com.foodsaver.app.commonModule.utils.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UIKit.UIApplication

// iosMain
actual class GoogleAuthenticator {
    internal actual suspend fun getGoogleIdToken(platformContext: PlatformContext): String? {
        return suspendCancellableCoroutine { continuation ->
            // Получаем корневой ViewController, поверх которого iOS покажет шторку Google
            val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController

            if (rootViewController == null) {
                // Если не нашли экран, возвращаем null (?) или можно кинуть ошибку
                continuation.resume(null) { cause, _, _ -> }
                return@suspendCancellableCoroutine
            }

            // Вызываем библиотеку GoogleSignIn
            GIDSignIn.sharedInstance.signInWithPresentingViewController(rootViewController) { result, error ->
                if (error != null) {
                    // Если пользователь отменил вход или произошла ошибка сети
                    println("Google Sign-In Error: ${error.localizedDescription}")
                    continuation.resume(null) { cause, _, _ -> }
                } else if (result != null) {
                    // Достаем ID Token
                    val idToken = result.user.idToken?.tokenString
                    continuation.resume(idToken) { cause, _, _ -> }
                } else {
                    continuation.resume(null) { cause, _, _ -> }
                }
            }
        }
    }
}