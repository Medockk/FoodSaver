package com.foodsaver.app.data.mappers

import com.foodsaver.app.data.dto.AuthResponseModelDto
import com.foodsaver.app.data.dto.SignInModelDto
import com.foodsaver.app.data.dto.SignUpModelDto
import com.foodsaver.app.domain.model.AuthResponseModel
import com.foodsaver.app.domain.model.SignInModel
import com.foodsaver.app.domain.model.SignUpModel

internal fun SignInModel.toDto() =
    SignInModelDto(username, password)

internal fun SignInModelDto.toModel() =
    SignInModel(username, password)

internal fun SignUpModel.toDto() =
    SignUpModelDto(username, password)

internal fun SignUpModelDto.toModel() =
    SignUpModel(username, password)

internal fun AuthResponseModelDto.toModel() =
    AuthResponseModel(uid, username, roles)