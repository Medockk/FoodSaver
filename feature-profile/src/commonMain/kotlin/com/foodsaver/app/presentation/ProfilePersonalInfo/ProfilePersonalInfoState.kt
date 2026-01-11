package com.foodsaver.app.presentation.ProfilePersonalInfo

import com.foodsaver.app.domain.model.UserModel

data class ProfilePersonalInfoState(
    val profile: UserModel? = null,
    val isLoading: Boolean = false,
    val showGallery: Boolean = false,

    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val bio: String = "",
)
