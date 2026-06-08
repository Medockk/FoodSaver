package com.foodsaver.app.feature.auth.presentation.utils

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.presentation.AppAction
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreFcm.service.FcmService
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class AuthenticationBaseViewModel<A: AppAction>(
    private val fcmService: FcmService,
    private val authManager: AuthUserManager
): BaseViewModel<A>() {

    protected open suspend fun onSaveFcmToken() {
        val token = fcmService.getFcmToken()
        token?.let { token ->
            withContext(NonCancellable) {
                try {
                    fcmService.saveFcmToken(token)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    protected open fun checkFields(fields: List<String>): Boolean {
        val fields = fields.map {
            it.isNotBlank()
        }
        return fields.contains(true)
    }

    protected open fun saveAuthenticationSession(userId: String) {
        authManager.setCurrentUid(userId)
        println("Remember user Saved user uid: ${authManager.getCurrentUid()}")
    }
}