package com.foodsaver.app.presentation.profilePersonalInfo

import com.foodsaver.app.coreProfile.domain.model.ProfileModel

data class ProfilePersonalInfoState(
    val profile: ProfileModel? = null,
    val isLoading: Boolean = false,
    val showGallery: Boolean = false,

    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val bio: String = "",
)
