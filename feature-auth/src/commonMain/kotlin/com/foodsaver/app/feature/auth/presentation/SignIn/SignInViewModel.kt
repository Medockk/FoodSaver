package com.foodsaver.app.feature.auth.presentation.SignIn

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.usecase.InsertUserUseCase
import com.foodsaver.app.feature.auth.domain.model.AuthResponseModel
import com.foodsaver.app.feature.auth.domain.model.SignInModel
import com.foodsaver.app.feature.auth.domain.usecase.AuthenticateWithGoogleUseCase
import com.foodsaver.app.feature.auth.domain.usecase.SignInUseCase
import com.foodsaver.app.feature.auth.domain.utils.EmailValidator
import com.foodsaver.app.feature.auth.presentation.SignIn.SignInAction.OnError
import com.foodsaver.app.feature.auth.presentation.SignIn.SignInAction.OnSuccessSignIn
import com.foodsaver.app.utils.ApiResult.ApiResult
import com.foodsaver.app.utils.InputOutput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val insertUserUseCase: InsertUserUseCase,

    private val signInWithGoogleUseCase: AuthenticateWithGoogleUseCase,
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
                            username = _state.value.email,
                            password = _state.value.password
                        )

                        _state.value = state.value.copy(isLoading = true)
                        when (val result = signInUseCase.invoke(request)) {
                            is ApiResult.Error -> {
                                _state.value = state.value.copy(isLoading = false)
                                _channel.send(OnError(result.error.message))
                            }

                            ApiResult.Loading -> Unit

                            is ApiResult.Success<AuthResponseModel> -> {
                                val insertResult = insertUserUseCase.invoke(
                                    UserModel(
                                        result.data.uid,
                                        result.data.username
                                    )
                                )
                                insertResult.onFailure {
                                    _channel.send(OnError(it.message ?: ""))
                                }

                                withContext(Dispatchers.Main) {
                                    _state.value = state.value.copy(
                                        isSuccess = true,
                                        isLoading = false
                                    )
                                    _channel.send(OnSuccessSignIn)
                                }
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

            SignInEvent.OnSignInWithGoogleClick -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    _state.value = state.value.copy(isLoading = false)
                    when (val result = signInWithGoogleUseCase.invoke()) {
                        is ApiResult.Error -> {
                            println(result.error)
                            withContext(Dispatchers.Main) {
                                _state.value = state.value.copy(isLoading = false)
                                _channel.send(OnError(result.error.message))
                            }
                        }

                        ApiResult.Loading -> Unit

                        is ApiResult.Success<AuthResponseModel> -> {
                            println(result.data)

                            withContext(Dispatchers.Main) {
                                _state.value = state.value.copy(
                                    isLoading = false,
                                    isSuccess = true
                                )
                                _channel.send(OnSuccessSignIn)
                            }
                        }
                    }
                }
            }
        }
    }
}