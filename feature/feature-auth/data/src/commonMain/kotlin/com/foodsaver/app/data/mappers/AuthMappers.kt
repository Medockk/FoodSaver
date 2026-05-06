package com.foodsaver.app.data.mappers

import com.foodsaver.app.data.dto.AuthResponseModelDto
import com.foodsaver.app.data.dto.ForgotPasswordDto
import com.foodsaver.app.data.dto.ResetPasswordDto
import com.foodsaver.app.data.dto.SignInModelDto
import com.foodsaver.app.data.dto.SignUpModelDto
import com.foodsaver.app.domain.model.AuthResponseModel
import com.foodsaver.app.domain.model.ForgotPasswordModel
import com.foodsaver.app.domain.model.ResetPasswordModel
import com.foodsaver.app.domain.model.SignInModel
import com.foodsaver.app.domain.model.SignUpModel

internal fun SignInModel.toDto() =
    SignInModelDto(email, password)

internal fun SignInModelDto.toModel() =
    SignInModel(email, password)

internal fun SignUpModel.toDto() =
    SignUpModelDto(fullName, email, password)

internal fun SignUpModelDto.toModel() =
    SignUpModel(email, password, fullName)

internal fun AuthResponseModelDto.toModel() =
    AuthResponseModel(uid, permissions)

internal fun ForgotPasswordModel.toDto() =
    ForgotPasswordDto(
        email = email
    )

internal fun ResetPasswordModel.toDto() =
    ResetPasswordDto(
        password = password,
        confirmPassword = confirmPassword
    )