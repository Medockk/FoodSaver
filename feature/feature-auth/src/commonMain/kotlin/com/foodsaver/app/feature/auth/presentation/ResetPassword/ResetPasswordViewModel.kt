package com.foodsaver.app.feature.auth.presentation.ResetPassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.commonModule.utils.stateFlow
import com.foodsaver.app.domain.model.ResetPasswordModel
import com.foodsaver.app.domain.usecase.ResetPasswordUseCase
import com.foodsaver.app.domain.utils.AuthExceptions
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    savedStateHandle: SavedStateHandle,
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : BaseViewModel<ResetPasswordAction>() {

    override val baseChannel: Channel<ResetPasswordAction> = Channel()
    override val channel = baseChannel.receiveAsFlow()

//    private val navArgs = savedStateHandle.toRoute<Route.AuthGraph.ResetPasswordScreen>()

    private val _state = MutableStateFlow(ResetPasswordState())
    val state = _state.stateFlow(ResetPasswordState())

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    val canReset = combine(_password, _confirmPassword) { currentPassword, currentConfirmPassword ->
        currentPassword == currentConfirmPassword
    }.stateFlow(false)

    fun onEvent(event: ResetPasswordEvent) {
        when (event) {
            is ResetPasswordEvent.OnConfirmPasswordChange -> {
                _confirmPassword.update { event.value }
            }

            ResetPasswordEvent.OnConfirmPasswordVisibilityChange -> {
                _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }

            is ResetPasswordEvent.OnPasswordChange -> {
                _password.update { event.value }
            }

            ResetPasswordEvent.OnPasswordVisibilityChange -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

//            ResetPasswordEvent.OnResetPasswordClick -> {
//                viewModelScope.launch(Dispatchers.InputOutput) {
//                    _state.update { it.copy(isLoading = true) }
//
//                    val resetPasswordModel = ResetPasswordModel(
//                        password = _password.value,
//                        confirmPassword = _confirmPassword.value,
//                        resetPasswordToken = navArgs.token
//                    )
//
//                    try {
//                        resetPasswordUseCase.invoke(resetPasswordModel)
//                            .onSuccess {
//                                baseChannel.send(ResetPasswordAction.OnSuccess)
//                            }.onFailure {
//                                sendError(it)
//                            }
//                    } catch (_: AuthExceptions.PasswordNotEquals) {
//                        sendError("Password not equals!")
//                    } finally {
//                        _state.update { it.copy(isLoading = false) }
//                    }
//                }
//            }
            else -> {}
        }
    }

    override fun mapBaseError(message: String): ResetPasswordAction =
        ResetPasswordAction.OnError(message)
}