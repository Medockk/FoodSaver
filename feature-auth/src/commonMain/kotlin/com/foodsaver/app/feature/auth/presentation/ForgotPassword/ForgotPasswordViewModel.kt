package com.foodsaver.app.feature.auth.presentation.ForgotPassword

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.ApiResult.onFailure
import com.foodsaver.app.commonModule.ApiResult.onSuccess
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.commonModule.utils.stateFlow
import com.foodsaver.app.domain.model.ForgotPasswordModel
import com.foodsaver.app.domain.usecase.ForgotPasswordUseCase
import com.foodsaver.app.domain.utils.AuthExceptions
import com.foodsaver.app.domain.utils.EmailValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
): BaseViewModel<ForgotPasswordAction>() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    val canResetPassword = _email
        .map {
            EmailValidator.validate(it)
        }.stateFlow(false)

    override val baseChannel: Channel<ForgotPasswordAction> = Channel()
    val channel = baseChannel.receiveAsFlow()

    override fun mapBaseError(message: String): ForgotPasswordAction {
        return ForgotPasswordAction.OnError(message)
    }

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.OnEmailChange -> {
                _email.update { event.value }
            }
            ForgotPasswordEvent.OnForgotPasswordClick -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    val forgotPasswordModel = ForgotPasswordModel(
                        email = _email.value
                    )
                    try {
                        forgotPasswordUseCase.invoke(forgotPasswordModel)
                            .onSuccess {
                                baseChannel.send(ForgotPasswordAction.OnSuccess)
                            }.onFailure {
                                sendError(it.message)
                            }
                    } catch (_: AuthExceptions.InvalidEmail) {
                        sendError("Invalid email address!")
                    }
                }
            }
        }
    }
}