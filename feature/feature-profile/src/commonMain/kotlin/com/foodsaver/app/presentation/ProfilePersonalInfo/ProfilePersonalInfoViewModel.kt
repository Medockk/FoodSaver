package com.foodsaver.app.presentation.ProfilePersonalInfo

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreProfile.domain.usecase.GetProfileUseCase
import com.foodsaver.app.domain.model.ProfilePersonalInfoModel
import com.foodsaver.app.domain.usecase.personalInfo.SavePersonalInfoUseCase
import com.foodsaver.app.domain.usecase.personalInfo.UploadAvatarUseCase
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
) : BaseViewModel<ProfilePersonalInfoAction>() {

    private val _state = MutableStateFlow(ProfilePersonalInfoState())
    val state = _state.asStateFlow()

    private var initialProfileData = ProfilePersonalInfoState().profile

    override val baseChannel: Channel<ProfilePersonalInfoAction> = Channel()
    override val channel = baseChannel.receiveAsFlow()

    init {
        getProfileInfo()
    }

    private fun getProfileInfo() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase.invoke().collectRequest(
                onSuccess = { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            profile = result
                        )
                    }
                    initialProfileData = result
                },
                onLoading = {
                    _state.update { it.copy(isLoading = true) }
                },
                onError = { error ->
                    _state.update { it.copy(isLoading = false) }
                    sendError(error)
                }
            )
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
                    trySendError("Full name must be not empty")
                    return
                } else if (_state.value.email.isBlank() && _state.value.profile?.email.isNullOrBlank()) {
                    trySendError("Email must be not empty")
                    return
                }


                if (
                    _state.value.fullName == initialProfileData?.name &&
                    _state.value.email == initialProfileData?.email &&
                    _state.value.phone == initialProfileData?.phone &&
                    _state.value.bio == initialProfileData?.bio
                ) {
                    baseChannel.trySend(ProfilePersonalInfoAction.OnSuccessSave)
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
                        val result = savePersonalInfoUseCase(request)
                        result.onFailure { error ->

                            _state.update { it.copy(isLoading = false) }
                            sendError(error)
                        }.onSuccess {

                            _state.update { it.copy(isLoading = false) }
                            baseChannel.send(ProfilePersonalInfoAction.OnSuccessSave)
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

    override fun mapBaseError(message: String): ProfilePersonalInfoAction {
        return ProfilePersonalInfoAction.OnError(message)
    }
}