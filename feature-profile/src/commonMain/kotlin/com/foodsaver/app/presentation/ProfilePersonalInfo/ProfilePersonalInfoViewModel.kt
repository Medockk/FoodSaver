package com.foodsaver.app.presentation.ProfilePersonalInfo

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.InputOutput
import com.foodsaver.app.domain.model.ProfilePersonalInfoModel
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.usecase.GetProfileUseCase
import com.foodsaver.app.domain.usecase.personalInfo.SavePersonalInfoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfilePersonalInfoViewModel(
    private val savePersonalInfoUseCase: SavePersonalInfoUseCase,
    private val getProfileUseCase: GetProfileUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(ProfilePersonalInfoState())
    val state: State<ProfilePersonalInfoState> = _state

    private var initialProfileData = ProfilePersonalInfoState().profile

    private val _channel = Channel<ProfilePersonalInfoAction>()
    val channel = _channel.receiveAsFlow()

    init {
        getProfileInfo()
    }

    private fun getProfileInfo() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase.invoke().collect {
                when (it) {
                    is ApiResult.Error -> {
                        _state.value = state.value.copy(isLoading = false)
                        _channel.send(ProfilePersonalInfoAction.OnError(it.error.message))
                    }

                    ApiResult.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }

                    is ApiResult.Success<UserModel> -> {
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(
                                isLoading = false,
                                profile = it.data,
                            )

                            initialProfileData = it.data
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: ProfilePersonalInfoEvent) {
        when (event) {
            is ProfilePersonalInfoEvent.OnBioChange -> {
                _state.value = state.value.copy(
                    bio = event.value
                )
            }

            is ProfilePersonalInfoEvent.OnEmailChange -> {
                _state.value = state.value.copy(
                    email = event.value
                )
            }

            is ProfilePersonalInfoEvent.OnFullNameChange -> {
                _state.value = state.value.copy(
                    fullName = event.value
                )
            }

            is ProfilePersonalInfoEvent.OnPhoneChange -> {
                _state.value = state.value.copy(
                    phone = event.value
                )
            }

            ProfilePersonalInfoEvent.OnChangeImage -> TODO()
            ProfilePersonalInfoEvent.OnSave -> {
                if (_state.value.fullName.isBlank() && _state.value.profile?.name.isNullOrBlank()) {
                    viewModelScope.launch {
                        _channel.send(ProfilePersonalInfoAction.OnError("Full name must be not empty"))
                    }
                    return
                } else if (_state.value.email.isBlank() && _state.value.profile?.email.isNullOrBlank()) {
                    viewModelScope.launch {
                        _channel.send(ProfilePersonalInfoAction.OnError("Email must be not empty"))
                    }
                    return
                }


                if (
                    _state.value.fullName == initialProfileData?.name &&
                    _state.value.email == initialProfileData?.email &&
                    _state.value.phone == initialProfileData?.phone &&
                    _state.value.bio == initialProfileData?.bio
                ) {
                    viewModelScope.launch {
                        _channel.send(ProfilePersonalInfoAction.OnSuccessSave)
                    }
                } else {
                    viewModelScope.launch(Dispatchers.InputOutput) {
                        _state.value = state.value.copy(isLoading = true)

                        val request = with(_state.value) {
                            ProfilePersonalInfoModel(
                                fullName = fullName.ifBlank { _state.value.profile?.name ?: "" },
                                email = email.ifBlank { _state.value.profile?.email ?: "" },
                                phone = phone.ifBlank { _state.value.profile?.phone ?: "" },
                                bio = bio.ifBlank { _state.value.profile?.bio ?: "" }
                            )
                        }
                        when (val result = savePersonalInfoUseCase(request)) {
                            is ApiResult.Error -> {
                                _state.value = state.value.copy(isLoading = false)
                                _channel.send(ProfilePersonalInfoAction.OnError(result.error.message))
                            }

                            ApiResult.Loading -> Unit
                            is ApiResult.Success<*> -> {
                                _state.value = state.value.copy(isLoading = false)
                                _channel.send(ProfilePersonalInfoAction.OnSuccessSave)
                            }
                        }
                    }
                }
            }
        }
    }
}