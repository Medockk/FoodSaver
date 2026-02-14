package com.foodsaver.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.foodsaver.app.commonModule.utils.stateFlow
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreSettings.domain.provider.DefaultLocaleProvider
import com.foodsaver.app.coreSettings.domain.repository.LocaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AppViewModel(
    private val isUserAuthenticated: AuthUserManager,
    private val localeRepository: LocaleRepository,
    defaultLocaleProvider: DefaultLocaleProvider
): ViewModel() {

    var isUserLogin by mutableStateOf(false)
        private set

    private val _currentLocale = MutableStateFlow(defaultLocaleProvider.getDefaultLocale())
    val currentLocale = _currentLocale
        .onStart {
            localeRepository.getCurrentLocale()
                .collect { locale ->
                    println("Locale from dataStore $locale")
                    _currentLocale.update { locale }
                }
        }.stateFlow(defaultLocaleProvider.getDefaultLocale())

    init {
        val result = isUserAuthenticated.isUserAuthenticated()
        isUserLogin = result
    }

    fun onUserAuthenticate(uid: String) {
        isUserAuthenticated.setCurrentUid(uid)
    }
}