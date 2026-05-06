package com.foodsaver.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.utils.stateFlow
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreSettings.domain.provider.DefaultLocaleProvider
import com.foodsaver.app.coreSettings.domain.repository.LocaleRepository
import com.foodsaver.app.manager.AccessTokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val authUserManager: AuthUserManager,
    private val accessTokenManager: AccessTokenManager,
    private val localeRepository: LocaleRepository,
    defaultLocaleProvider: DefaultLocaleProvider
): ViewModel() {

    private val _authenticationState = MutableStateFlow<AuthenticationState>(AuthenticationState.Loading)
    val authenticationState = _authenticationState.asStateFlow()

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
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            val hasRefreshToken = async { accessTokenManager.getRefreshToken() != null }
            val hasJwt = async { accessTokenManager.getJwtToken() != null }
            val hasUid = async { authUserManager.isUserAuthenticated() }

            if (hasRefreshToken.await() && hasUid.await()) {
                println("Remember user remind uid: ${authUserManager.getCurrentUid()}")
                _authenticationState.update { AuthenticationState.Authenticated }
                return@launch
            }

            _authenticationState.update { AuthenticationState.Unauthenticated }
        }
    }
}