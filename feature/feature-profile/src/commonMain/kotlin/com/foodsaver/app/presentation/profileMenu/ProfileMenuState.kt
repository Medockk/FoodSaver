package com.foodsaver.app.presentation.profileMenu

import com.foodsaver.app.coreProfile.domain.model.ProfileModel

data class ProfileMenuState(
    val isLoading: Boolean = false,

    val profile: ProfileModel? = null,
)
