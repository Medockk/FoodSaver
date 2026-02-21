package com.foodsaver.app.presentation.ProfileMenu

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreProfile.domain.usecase.GetProfileUseCase
import com.foodsaver.app.coreSettings.domain.repository.LocaleRepository
import com.foodsaver.app.domain.usecase.auth.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val localeRepository: LocaleRepository
): BaseViewModel<ProfileAction>() {

    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    override val baseChannel: Channel<ProfileAction> = Channel()
    val channel = baseChannel.receiveAsFlow()

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase().collectRequest(
                onSuccess = {
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            isLoading = false,
                            profile = it
                        )
                    }
                },
                onLoading = {
                    _state.value = state.value.copy(isLoading = true)
                },
                onError = {
                    _state.value = state.value.copy(isLoading = false)
                    sendError(it.message)
                }
            )
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnLogOutClick -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    when (val result = logoutUseCase()) {
                        is ApiResult.Error -> {
                            _state.value = state.value.copy(isLoading = false)
                            sendError(result.error.message)
                        }
                        ApiResult.Loading -> {
                            _state.value = state.value.copy(isLoading = true)
                        }
                        is ApiResult.Success<*> -> {
                            _state.value = state.value.copy(isLoading = false)
                            baseChannel.send(ProfileAction.OnSuccessLogout)
                        }
                    }
                }
            }

            is ProfileEvent.OnChangleLocaleClick -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    localeRepository.setCurrentLocale(
                        languageCode = event.locale.value
                    )
                }
            }
        }
    }

    override fun mapBaseError(message: String): ProfileAction {
        return ProfileAction.OnError(message)
    }
}