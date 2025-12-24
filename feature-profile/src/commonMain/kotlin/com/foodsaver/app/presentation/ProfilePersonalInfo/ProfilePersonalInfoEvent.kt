package com.foodsaver.app.presentation.ProfilePersonalInfo

sealed interface ProfilePersonalInfoEvent {

    data class OnFullNameChange(val value: String): ProfilePersonalInfoEvent
    data class OnEmailChange(val value: String): ProfilePersonalInfoEvent
    data class OnPhoneChange(val value: String): ProfilePersonalInfoEvent
    data class OnBioChange(val value: String): ProfilePersonalInfoEvent

    data object OnChangeImage: ProfilePersonalInfoEvent
    data object OnSave: ProfilePersonalInfoEvent
}