package com.foodsaver.app.presentation.ProfilePersonalInfo

sealed interface ProfilePersonalInfoEvent {

    data class OnFullNameChange(val value: String): ProfilePersonalInfoEvent
    data class OnEmailChange(val value: String): ProfilePersonalInfoEvent
    data class OnPhoneChange(val value: String): ProfilePersonalInfoEvent
    data class OnBioChange(val value: String): ProfilePersonalInfoEvent

    class OnChangeImage(val bytes: ByteArray, val contentType: String? = null): ProfilePersonalInfoEvent
    data object OnOpenGallery: ProfilePersonalInfoEvent
    data object OnCloseGallery: ProfilePersonalInfoEvent
    data object OnSave: ProfilePersonalInfoEvent
}