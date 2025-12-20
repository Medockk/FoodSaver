package com.foodsaver.app.presentation.ProfileMenu

import com.foodsaver.app.domain.model.UserModel

data class ProfileState(
    val isLoading: Boolean = false,

    val profile: UserModel? = null,
)
