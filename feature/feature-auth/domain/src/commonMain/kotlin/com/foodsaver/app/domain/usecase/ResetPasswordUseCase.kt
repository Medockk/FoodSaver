package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.model.ResetPasswordModel
import com.foodsaver.app.domain.repository.AuthRepository
import com.foodsaver.app.domain.utils.AuthExceptions

class ResetPasswordUseCase(
    private val repository: AuthRepository
) {

    @Throws(AuthExceptions.PasswordNotEquals::class)
    suspend operator fun invoke(resetPasswordModel: ResetPasswordModel) =
        if (resetPasswordModel.password == resetPasswordModel.confirmPassword) repository.resetPassword(resetPasswordModel)
        else throw AuthExceptions.PasswordNotEquals()
}