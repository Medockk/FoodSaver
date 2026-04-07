package com.foodsaver.app.feature.auth.presentation.Auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onLocalFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreFcm.service.FcmService
import com.foodsaver.app.domain.model.SignInModel
import com.foodsaver.app.domain.model.SignUpModel
import com.foodsaver.app.domain.usecase.AuthenticateWithGoogleUseCase
import com.foodsaver.app.domain.usecase.SignInUseCase
import com.foodsaver.app.domain.usecase.SignUpUseCase
import com.foodsaver.app.domain.utils.AuthExceptions
import com.foodsaver.app.domain.utils.EmailValidator
import com.foodsaver.app.feature.auth.common.AuthLocalError
import com.foodsaver.app.feature.auth.common.AuthLocalError.Companion.fromException
import com.foodsaver.app.feature.auth.presentation.Auth.AuthAction.OnError
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val authenticateWithGoogleUseCase: AuthenticateWithGoogleUseCase,
    private val fcmService: FcmService,
) : BaseViewModel<AuthAction>() {

    private val _state = mutableStateOf(AuthState())
    val state: State<AuthState> = _state

    override val baseChannel: Channel<AuthAction> = Channel()
    override val channel = baseChannel.receiveAsFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnEmailChange -> {
                _state.value = state.value.copy(
                    email = event.value
                )
            }

            is AuthEvent.OnFioChange -> {
                _state.value = state.value.copy(
                    fio = event.value
                )
            }

            is AuthEvent.OnPasswordChange -> {
                _state.value = state.value.copy(
                    password = event.value
                )
            }

            AuthEvent.OnPasswordVisibilityChange -> {
                _state.value = state.value.copy(
                    isPasswordVisible = !_state.value.isPasswordVisible
                )
            }

            AuthEvent.OnSignInClick -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    if (!checkInputData()) return@launch

                    _state.value = state.value.copy(isLoading = true)
                    val result = signInUseCase.invoke(
                        signInModel = SignInModel(
                            username = _state.value.email,
                            password = _state.value.password
                        )
                    )

                    result.onFailure { error ->
                        _state.value = state.value.copy(isLoading = false)
                        sendError(error)
                    }.onSuccess { data ->
                        getAndSaveFcmToken().await()
                        _state.value = state.value.copy(isLoading = false)
                        baseChannel.send(AuthAction.OnSuccessAuthentication(data.uid))
                    }
                }
            }

            AuthEvent.OnSignUpClick -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    if (!checkInputData(true)) return@launch

                    _state.value = state.value.copy(isLoading = true)
                    val result = signUpUseCase.invoke(
                        signUpModel = SignUpModel(
                            username = _state.value.email,
                            password = _state.value.password
                        )
                    )
                    result.onFailure { error ->

                        _state.value = state.value.copy(isLoading = false)
                        sendError(error)
                    }.onSuccess { data ->
                        getAndSaveFcmToken().await()
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(isLoading = false)
                        }

                        baseChannel.send(AuthAction.OnSuccessAuthentication(data.uid))
                    }
                }
            }

            is AuthEvent.OnAuthenticateWithGoogle -> {
                _state.value = state.value.copy(isLoading = true)
                viewModelScope.launch(Dispatchers.InputOutput) {
                    val result = authenticateWithGoogleUseCase.invoke(
                        event.platformContext
                    )
                    result.onFailure { error ->
                        _state.value = state.value.copy(isLoading = false)

                        error.onLocalFailure {
                            if (it.localError is AuthExceptions) {
                                val authExceptions = it.localError as AuthExceptions
                                val localError = authExceptions.fromException()
                                sendError(ApiResult.Error.Local(localError))
                                return@onFailure
                            }
                        }

                        sendError(error)
                    }.onSuccess { data ->
                        println("FCM before sending token")
                        getAndSaveFcmToken().await()
                        println("FCM after after sending token")

                        _state.value = state.value.copy(isLoading = false)
                        baseChannel.send(AuthAction.OnSuccessAuthentication(data.uid))
                    }
                }
            }

            is AuthEvent.OnTabRowIndexChange -> {
                _state.value = state.value.copy(
                    tabRowIndex = event.value,
                    authPage = if (event.value == 0) AuthPage.SIGN_UP
                    else AuthPage.SIGN_IN
                )
            }
        }
    }

    private suspend fun checkInputData(shouldCheckFio: Boolean = false): Boolean {
        if (shouldCheckFio && _state.value.fio.isBlank()) {
            sendError(ApiResult.Error.Local(AuthLocalError.EmptyFio))
            return false
        }
        if (_state.value.email.isBlank()) {
            sendError(ApiResult.Error.Local(AuthLocalError.EmptyEmail))
            return false
        }
        if (!EmailValidator.validate(_state.value.email)) {
            sendError(ApiResult.Error.Local(AuthLocalError.InvalidEmail))
            return false
        }
        if (_state.value.password.isBlank()) {
            sendError(ApiResult.Error.Local(AuthLocalError.EmptyPassword))
            return false
        }

        return true
    }

    private fun getAndSaveFcmToken(): Deferred<Unit> {
        return viewModelScope.async {
            fcmService.getFcmToken { token ->
                println("FCM token in viewModel $token")
                token?.let { token ->
                    launch {
                        fcmService.saveFcmToken(token)
                        println("FCM sending after token")
                    }
                }
            }
        }
    }

    override fun mapBaseError(message: String): AuthAction {
        return OnError(message)
    }
}