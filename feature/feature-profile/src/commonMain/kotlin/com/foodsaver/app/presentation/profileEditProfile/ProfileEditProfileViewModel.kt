package com.foodsaver.app.presentation.profileEditProfile

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreProfile.domain.model.ProfileModel
import com.foodsaver.app.coreProfile.domain.model.UpdateProfileRequest
import com.foodsaver.app.coreProfile.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileEditProfileViewModel(
    private val profileRepository: ProfileRepository
): BaseViewModel<ProfileEditProfileAction>() {

    private val _state = MutableStateFlow(ProfileEditProfileState())
    val state = _state.asStateFlow()

    private var initialProfile: ProfileModel? = null

    init {
        observeProfile()
    }

    private fun observeProfile() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            profileRepository.observeProfile().collectRequest(
                onSuccess =  { profile ->
                    _state.update { it.copy(
                        fullName = profile.fullName,
                        email = profile.email,
                        phone = profile.phone ?: "",
                        bio = profile.bio ?: "",
                        avatarUri = profile.imageUri
                    ) }

                    initialProfile = profile
                }
            )
        }
    }

    fun onEvent(event: ProfileEditProfileEvent) {
        when (event) {
            is ProfileEditProfileEvent.OnAvatarChange -> {
                _state.update { it.copy(
                    avatarImageBitmap = event.bitmap,
                    avatarImageByteArray = ProfileEditProfileState.Avatar(event.byteArray),
                    isGalleryVisible = false
                ) }
            }
            is ProfileEditProfileEvent.OnBioChange -> {
                _state.update { it.copy(
                    bio = event.value
                ) }
            }
            is ProfileEditProfileEvent.OnEmailChange -> {
                _state.update { it.copy(
                    email = event.value
                ) }
            }
            is ProfileEditProfileEvent.OnFullNameChange -> {
                _state.update { it.copy(
                    fullName = event.value
                ) }
            }
            is ProfileEditProfileEvent.OnPhoneChange -> {
                _state.update { it.copy(
                    phone = event.value
                ) }
            }
            ProfileEditProfileEvent.OnSave -> {
                val currentState = _state.value
                val fullName = if (initialProfile?.fullName != currentState.fullName) {
                    currentState.fullName
                } else null
                val email = if (initialProfile?.email != currentState.email) {
                    currentState.email
                } else null
                val phone = if (initialProfile?.phone != currentState.phone) {
                    currentState.phone
                } else null
                val bio = if (initialProfile?.bio != currentState.bio) {
                    currentState.bio
                } else null

                val request = UpdateProfileRequest(
                    fullName = fullName,
                    phone = phone,
                    bio = bio,
                    email = email
                )

                viewModelScope.launch {
                    profileRepository.updateProfile(
                        request = request,
                        avatar = currentState.avatarImageByteArray?.bytes
                    )
                }
            }
            is ProfileEditProfileEvent.OnImagePicker -> {
                _state.update { it.copy(
                    isGalleryVisible = event.isVisible
                ) }
            }
        }
    }

    override fun mapBaseError(message: String): ProfileEditProfileAction {
        return ProfileEditProfileAction.OnError(message)
    }
}