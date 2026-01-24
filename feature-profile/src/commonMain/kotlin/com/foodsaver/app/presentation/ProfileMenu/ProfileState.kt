package com.foodsaver.app.presentation.ProfileMenu

import com.foodsaver.app.coreProfile.domain.model.UserModel

data class ProfileState(
    val isLoading: Boolean = false,

    val profile: UserModel? = null,
)
