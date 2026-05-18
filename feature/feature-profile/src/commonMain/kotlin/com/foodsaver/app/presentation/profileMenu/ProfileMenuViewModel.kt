package com.foodsaver.app.presentation.profileMenu

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onLoading
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreProfile.domain.usecase.GetProfileUseCase
import com.foodsaver.app.coreSettings.domain.repository.LocaleRepository
import com.foodsaver.app.domain.usecase.auth.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileMenuViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val localeRepository: LocaleRepository,
) : BaseViewModel<ProfileMenuAction>() {

    private val _state = MutableStateFlow(ProfileMenuState())
    val state = _state.asStateFlow()

    override val baseChannel: Channel<ProfileMenuAction> = Channel()
    override val channel = baseChannel.receiveAsFlow()

    init {
        observeProfile()
    }

    private fun observeProfile() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase().collectRequest(
                onSuccess = { profile ->
                    _state.update { it.copy(
                        isLoading = false,
                        profile = profile
                    ) }
                }
            )
        }
    }

    fun onEvent(event: ProfileMenuEvent) {
        when (event) {
            ProfileMenuEvent.OnLogOutClick -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    val result = logoutUseCase()
                    result.onFailure { error ->
                        _state.update { it.copy(isLoading = false) }
                        sendError(error)
                    }.onLoading {
                        _state.update { it.copy(isLoading = true) }
                    }.onSuccess {
                        _state.update { it.copy(isLoading = false) }
                        baseChannel.send(ProfileMenuAction.OnSuccessLogout)
                    }
                }
            }

            is ProfileMenuEvent.OnChangleLocaleClick -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    localeRepository.setCurrentLocale(
                        languageCode = event.locale.value
                    )
                }
            }
        }
    }

    override fun mapBaseError(message: String): ProfileMenuAction {
        return ProfileMenuAction.OnError(message)
    }
}