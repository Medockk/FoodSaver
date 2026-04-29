package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.model.ResetPasswordModel
import com.foodsaver.app.domain.repository.AuthRepository
import com.foodsaver.app.domain.utils.AuthExceptions
import kotlin.coroutines.cancellation.CancellationException // Не забудь импорт

class ResetPasswordUseCase(
    private val repository: AuthRepository
) {

    @Throws(AuthExceptions.PasswordNotEquals::class, CancellationException::class) // Добавили сюда
    suspend operator fun invoke(resetPasswordModel: ResetPasswordModel) =
        if (resetPasswordModel.password == resetPasswordModel.confirmPassword) {
            repository.resetPassword(resetPasswordModel)
        } else {
            throw AuthExceptions.PasswordNotEquals()
        }
}
