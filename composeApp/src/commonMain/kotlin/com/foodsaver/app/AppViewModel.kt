package com.foodsaver.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.foodsaver.app.coreAuth.AuthUserManager

class AppViewModel(
    private val isUserAuthenticated: AuthUserManager
): ViewModel() {

    var isUserLogin by mutableStateOf(false)
        private set

    init {
        val result = isUserAuthenticated.isUserAuthenticated()
        isUserLogin = result
    }

    fun onUserAuthenticate(uid: String) {
        isUserAuthenticated.setCurrentUid(uid)
    }
}