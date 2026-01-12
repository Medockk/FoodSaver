package com.foodsaver.app.presentation.ProfilePersonalInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.domain.model.ProfilePersonalInfoModel
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.usecase.GetProfileUseCase
import com.foodsaver.app.domain.usecase.personalInfo.SavePersonalInfoUseCase
import com.foodsaver.app.domain.usecase.personalInfo.UploadAvatarUseCase
import com.foodsaver.app.presentation.ProfilePersonalInfo.ProfilePersonalInfoAction.OnError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfilePersonalInfoViewModel(
    private val savePersonalInfoUseCase: SavePersonalInfoUseCase,
    private val getProfileUseCase: GetProfileUseCase,

    private val uploadAvatarUseCase: UploadAvatarUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfilePersonalInfoState())
    val state = _state.asStateFlow()

    private var initialProfileData = ProfilePersonalInfoState().profile

    private val _channel = Channel<ProfilePersonalInfoAction>()
    val channel = _channel.receiveAsFlow()

    init {
        getProfileInfo()
    }

    private fun getProfileInfo() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase.invoke().collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        _channel.send(OnError(result.error.message))
                    }

                    ApiResult.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }

                    is ApiResult.Success<UserModel> -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                profile = result.data,
                            )
                        }
                        initialProfileData = result.data
                    }
                }
            }
        }
    }

    fun onEvent(event: ProfilePersonalInfoEvent) {
        when (event) {
            is ProfilePersonalInfoEvent.OnBioChange -> {
                _state.update { it.copy(bio = event.value) }
            }

            is ProfilePersonalInfoEvent.OnEmailChange -> {
                _state.update { it.copy(email = event.value) }
            }

            is ProfilePersonalInfoEvent.OnFullNameChange -> {
                _state.update { it.copy(fullName = event.value) }
            }

            is ProfilePersonalInfoEvent.OnPhoneChange -> {
                _state.update { it.copy(phone = event.value) }
            }

            is ProfilePersonalInfoEvent.OnChangeImage -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    uploadAvatarUseCase.invoke(event.bytes, event.contentType ?: "image/png")

                    _state.update { it.copy(showGallery = false) }
                }
            }

            ProfilePersonalInfoEvent.OnSave -> {
                if (_state.value.fullName.isBlank() && _state.value.profile?.name.isNullOrBlank()) {
                    _channel.trySend(OnError("Full name must be not empty"))
                    return
                } else if (_state.value.email.isBlank() && _state.value.profile?.email.isNullOrBlank()) {
                    _channel.trySend(OnError("Email must be not empty"))
                    return
                }


                if (
                    _state.value.fullName == initialProfileData?.name &&
                    _state.value.email == initialProfileData?.email &&
                    _state.value.phone == initialProfileData?.phone &&
                    _state.value.bio == initialProfileData?.bio
                ) {
                    _channel.trySend(ProfilePersonalInfoAction.OnSuccessSave)
                } else {
                    viewModelScope.launch(Dispatchers.InputOutput) {
                        _state.update { it.copy(isLoading = true) }

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
                                _state.update { it.copy(isLoading = false) }
                                _channel.send(OnError(result.error.message))
                            }

                            ApiResult.Loading -> Unit
                            is ApiResult.Success<*> -> {
                                _state.update { it.copy(isLoading = false) }
                                _channel.send(ProfilePersonalInfoAction.OnSuccessSave)
                            }
                        }
                    }
                }
            }

            ProfilePersonalInfoEvent.OnCloseGallery -> {
                _state.update { it.copy(showGallery = false) }
            }
            ProfilePersonalInfoEvent.OnOpenGallery -> {
                _state.update { it.copy(showGallery = true) }
            }
        }
    }
}