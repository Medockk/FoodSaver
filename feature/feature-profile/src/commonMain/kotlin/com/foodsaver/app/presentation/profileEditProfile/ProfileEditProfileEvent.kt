package com.foodsaver.app.presentation.profileEditProfile

import androidx.compose.ui.graphics.ImageBitmap

sealed interface ProfileEditProfileEvent {

    data class OnFullNameChange(val value: String): ProfileEditProfileEvent
    data class OnEmailChange(val value: String): ProfileEditProfileEvent
    data class OnPhoneChange(val value: String): ProfileEditProfileEvent
    data class OnBioChange(val value: String): ProfileEditProfileEvent
    class OnAvatarChange(
        val bitmap: ImageBitmap,
        val byteArray: ByteArray
    ): ProfileEditProfileEvent

    data class OnImagePicker(val isVisible: Boolean): ProfileEditProfileEvent
    data object OnSave: ProfileEditProfileEvent
}