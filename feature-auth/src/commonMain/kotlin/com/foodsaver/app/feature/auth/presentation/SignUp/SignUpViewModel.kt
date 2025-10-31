package com.foodsaver.app.feature.auth.presentation.SignUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.feature.auth.domain.model.AuthResponseModel
import com.foodsaver.app.feature.auth.domain.model.SignUpModel
import com.foodsaver.app.feature.auth.domain.usecase.SignUpUseCase
import com.foodsaver.app.feature.auth.domain.utils.EmailValidator
import com.foodsaver.app.utils.ApiResult.ApiResult
import com.foodsaver.app.utils.InputOutput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
): ViewModel() {

    var state by mutableStateOf(SignUpState())
        private set

    private val _channel = Channel<SignUpAction>()
    val channel = _channel.receiveAsFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnEmailChange -> {
                state = state.copy(
                    email = event.value
                )
            }
            is SignUpEvent.OnNameChange -> {
                state = state.copy(
                    name = event.value
                )
            }
            is SignUpEvent.OnPasswordChange -> {
                state = state.copy(
                    password = event.value
                )
            }
            SignUpEvent.OnPasswordVisibilityChange -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }
            SignUpEvent.OnResetExceptionClick -> {
                state = state.copy(
                    exception = ""
                )
            }
            SignUpEvent.OnSignUpClick -> {
                if (EmailValidator.validate(state.email)) {
                    viewModelScope.launch(Dispatchers.InputOutput) {
                        val request = SignUpModel(
                            email = state.email,
                            password = state.password
                        )

                        state = state.copy(isLoading = true)
                        when (val result = signUpUseCase.invoke(request)) {
                            is ApiResult.Error -> {
                                state = state.copy(isLoading = false)
                                _channel.send(SignUpAction.OnError(result.error.message))
                            }
                            ApiResult.Loading -> Unit
                            is ApiResult.Success<AuthResponseModel> -> {
                                state = state.copy(isLoading = false, isSuccess = true)
                                _channel.send(SignUpAction.OnSuccessSignUp)
                            }
                        }
                    }
                } else {
                    state = state.copy(
                        exception = "Email invalid",
                        isLoading = false
                    )
                }
            }
        }
    }
}