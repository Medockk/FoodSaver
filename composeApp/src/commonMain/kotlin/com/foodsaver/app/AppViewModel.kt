package com.foodsaver.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.foodsaver.app.utils.IsUserAuthenticated

class AppViewModel(
    private val isUserAuthenticated: IsUserAuthenticated
): ViewModel() {

    var isUserLogin by mutableStateOf(false)
        private set

    init {
        val result = isUserAuthenticated()
        isUserLogin = result
    }

    fun onUserAuthenticate() {
        isUserAuthenticated(true)
    }
}