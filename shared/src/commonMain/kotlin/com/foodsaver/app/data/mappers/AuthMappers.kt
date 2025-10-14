package com.foodsaver.app.data.mappers

import com.foodsaver.app.data.dto.Auth.AuthResponseModelDto
import com.foodsaver.app.data.dto.Auth.SignInModelDto
import com.foodsaver.app.data.dto.Auth.SignUpModelDto
import com.foodsaver.app.domain.model.Auth.AuthResponseModel
import com.foodsaver.app.domain.model.Auth.SignInModel
import com.foodsaver.app.domain.model.Auth.SignUpModel

internal fun SignInModel.toDto() =
    SignInModelDto(email, password)

internal fun SignUpModel.toDto() =
    SignUpModelDto(email, password, displayName)

internal fun AuthResponseModelDto.toModel() =
    AuthResponseModel(uid, email)