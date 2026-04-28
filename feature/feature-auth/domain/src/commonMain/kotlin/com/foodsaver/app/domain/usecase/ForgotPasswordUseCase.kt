package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.model.ForgotPasswordModel
import com.foodsaver.app.domain.repository.AuthRepository
import com.foodsaver.app.domain.utils.AuthExceptions
import com.foodsaver.app.domain.utils.EmailValidator
import kotlin.coroutines.cancellation.CancellationException

class ForgotPasswordUseCase(
    private val repository: AuthRepository
) {

    @Throws(AuthExceptions.InvalidEmail::class, CancellationException::class)
    suspend operator fun invoke(forgotPasswordModel: ForgotPasswordModel) =
        if (EmailValidator.validate(forgotPasswordModel.email)) repository.forgotPassword(forgotPasswordModel)
        else throw AuthExceptions.InvalidEmail()
}
