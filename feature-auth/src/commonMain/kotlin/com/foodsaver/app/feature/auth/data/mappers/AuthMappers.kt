package com.foodsaver.app.feature.auth.data.mappers

import com.foodsaver.app.feature.auth.data.dto.AuthResponseModelDto
import com.foodsaver.app.feature.auth.data.dto.SignInModelDto
import com.foodsaver.app.feature.auth.data.dto.SignUpModelDto
import com.foodsaver.app.feature.auth.domain.model.AuthResponseModel
import com.foodsaver.app.feature.auth.domain.model.SignInModel
import com.foodsaver.app.feature.auth.domain.model.SignUpModel

internal fun SignInModel.toDto() =
    SignInModelDto(email, password)

internal fun SignInModelDto.toModel() =
    SignInModel(email, password)

internal fun SignUpModel.toDto() =
    SignUpModelDto(email, password)

internal fun SignUpModelDto.toModel() =
    SignUpModel(email, password)

internal fun AuthResponseModelDto.toModel() =
    AuthResponseModel(
        uid, email, roles
    )