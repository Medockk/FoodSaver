package com.foodsaver.app.feature.auth.presentation.utils

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.presentation.AppAction
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreFcm.service.FcmService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class AuthenticationBaseViewModel<A: AppAction>(
    private val fcmService: FcmService
): BaseViewModel<A>() {

    protected open suspend fun onSaveFcmToken() {
        viewModelScope.async {
            fcmService.getFcmToken { token ->
                if (token != null) {
                    launch {
                        fcmService.saveFcmToken(token)
                    }
                }
            }
        }.await()
    }

    protected open fun checkFields(fields: List<String>): Boolean {
        val fields = fields.map {
            it.isNotBlank()
        }
        return fields.contains(true)
    }
}