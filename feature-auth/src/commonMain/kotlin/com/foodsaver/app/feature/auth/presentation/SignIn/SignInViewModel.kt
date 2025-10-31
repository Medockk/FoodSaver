package com.foodsaver.app.feature.auth.presentation.SignIn

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.usecase.InsertUserUseCase
import com.foodsaver.app.feature.auth.domain.model.AuthResponseModel
import com.foodsaver.app.feature.auth.domain.model.SignInModel
import com.foodsaver.app.feature.auth.domain.usecase.SignInUseCase
import com.foodsaver.app.feature.auth.domain.utils.EmailValidator
import com.foodsaver.app.utils.ApiResult.ApiResult
import com.foodsaver.app.utils.InputOutput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val insertUserUseCase: InsertUserUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(SignInState())
    val state: State<SignInState> = _state

    private val _channel = Channel<SignInAction>()
    val channel = _channel.receiveAsFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.OnEmailChange -> {
                _state.value = state.value.copy(
                    email = event.value,
                )
            }

            is SignInEvent.OnPasswordChange -> {
                _state.value = state.value.copy(
                    password = event.value,
                )
            }

            SignInEvent.OnPasswordVisibilityChange -> {
                _state.value = state.value.copy(
                    isPasswordVisible = !_state.value.isPasswordVisible,
                )
            }

            SignInEvent.OnResetExceptionClick -> {
                _state.value = state.value.copy(
                    exception = ""
                )
            }

            SignInEvent.OnSignInClick -> {
                if (EmailValidator.validate(_state.value.email)) {
                    viewModelScope.launch(Dispatchers.InputOutput + SupervisorJob()) {
                        val request = SignInModel(
                            email = _state.value.email,
                            password = _state.value.password
                        )

                        _state.value = state.value.copy(isLoading = true)
                        when (val result = signInUseCase.invoke(request)) {
                            is ApiResult.Error -> {
                                _state.value = state.value.copy(isLoading = false)
                                _channel.send(SignInAction.OnError(result.error.message))
                            }

                            ApiResult.Loading -> {
                                _state.value = state.value.copy(
                                    isLoading = true
                                )
                            }

                            is ApiResult.Success<AuthResponseModel> -> {
                                _state.value = state.value.copy(
                                    isLoading = false
                                )

                                val insertResult = insertUserUseCase.invoke(
                                    UserModel(
                                        result.data.uid,
                                        result.data.email
                                    )
                                )
                                insertResult.onFailure {
                                    _channel.send(SignInAction.OnError(it.message ?: ""))
                                }

                                _state.value = state.value.copy(isSuccess = true)
                                _channel.send(SignInAction.OnSuccessSignIn)
                            }
                        }
                    }
                } else {
                    _state.value = state.value.copy(
                        exception = "Email invalid",
                        isLoading = false
                    )
                }
            }
        }
    }
}