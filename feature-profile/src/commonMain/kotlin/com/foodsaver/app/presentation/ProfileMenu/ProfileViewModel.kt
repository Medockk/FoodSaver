package com.foodsaver.app.presentation.ProfileMenu

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.InputOutput
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.usecase.GetProfileUseCase
import com.foodsaver.app.domain.usecase.auth.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    private val _channel = Channel<ProfileAction>()
    val channel = _channel.receiveAsFlow()

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase().collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _state.value = state.value.copy(isLoading = false)
                        _channel.send(ProfileAction.OnError(result.error.message))
                    }
                    ApiResult.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }
                    is ApiResult.Success<UserModel> -> {
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(
                                isLoading = false,
                                profile = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnLogOutClick -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    when (val result = logoutUseCase()) {
                        is ApiResult.Error -> {
                            _state.value = state.value.copy(isLoading = false)
                            _channel.send(ProfileAction.OnError(result.error.message))
                        }
                        ApiResult.Loading -> {
                            _state.value = state.value.copy(isLoading = true)
                        }
                        is ApiResult.Success<*> -> {
                            _state.value = state.value.copy(isLoading = false)
                            _channel.send(ProfileAction.OnSuccessLogout)
                        }
                    }
                }
            }
        }
    }
}