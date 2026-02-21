package com.foodsaver.app.data.mappers

import com.foodsaver.app.data.dto.ProfilePersonalInfoDto
import com.foodsaver.app.domain.model.ProfilePersonalInfoModel

internal fun ProfilePersonalInfoModel.toDto() =
    ProfilePersonalInfoDto(
        fullName = fullName,
        email = email,
        phone = phone,
        bio = bio,
        photoUrl = null
    )