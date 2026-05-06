package com.foodsaver.app.feature.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onLocalFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreFcm.service.FcmService
import com.foodsaver.app.domain.model.SignInModel
import com.foodsaver.app.domain.usecase.AuthenticateWithGoogleUseCase
import com.foodsaver.app.domain.usecase.SignInUseCase
import com.foodsaver.app.domain.utils.AuthExceptions
import com.foodsaver.app.feature.auth.common.AuthLocalError
import com.foodsaver.app.feature.auth.common.AuthLocalError.Companion.fromException
import com.foodsaver.app.feature.auth.presentation.login.LoginAction.*
import com.foodsaver.app.feature.auth.presentation.utils.AuthenticationBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    fcmService: FcmService,
    authManager: AuthUserManager,
    private val signInUseCase: SignInUseCase,
    private val authenticateWithGoogleUseCase: AuthenticateWithGoogleUseCase,
) : AuthenticationBaseViewModel<LoginAction>(fcmService, authManager) {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.ChangePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is LoginEvent.OnEmailValueChange -> {
                _state.update { it.copy(email = event.value) }
            }

            LoginEvent.OnLogin -> {
                val isFieldsNotEmpty = checkFields(
                    fields = listOf(
                        _state.value.email,
                        _state.value.password,
                    )
                )

                val currentState = _state.value

                if (currentState.email.isBlank()) {
                    val error = AuthLocalError.EmptyEmail
                    sendError(ApiResult.Error.Local(error))
                    return
                }
                if (currentState.password.isBlank()) {
                    val error = AuthLocalError.EmptyPassword
                    sendError(ApiResult.Error.Local(error))
                    return
                }

                if (isFieldsNotEmpty) {
                    _state.update { it.copy(isLoading = true) }

                    viewModelScope.launch(Dispatchers.InputOutput) {
                        val requestModel = SignInModel(_state.value.email, _state.value.password)
                        signInUseCase(requestModel)
                            .onSuccess { responseModel ->
                                onSaveFcmToken()
                                _state.update { it.copy(isLoading = false) }

                                if (_state.value.isRememberMe) {
                                    saveAuthenticationSession(responseModel.uid)
                                    println("Remember user with uid: ${responseModel.uid}")
                                }

                                baseChannel.send(OnLogged)
                            }.onFailure { error ->
                                _state.update { it.copy(isLoading = false) }
                                sendError(error)
                            }
                    }
                } else {
                    trySendError("Something empty")
                }
            }

            is LoginEvent.OnPasswordValueChange -> {
                _state.update { it.copy(password = event.value) }
            }

            is LoginEvent.OnLoginWithGoogle -> {

                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch(Dispatchers.InputOutput) {
                    authenticateWithGoogleUseCase(event.platformContext)
                        .onSuccess { responseModel ->
                            onSaveFcmToken()
                            saveAuthenticationSession(responseModel.uid)
                            _state.update { it.copy(isLoading = false) }
                            baseChannel.send(OnLogged)
                        }.onFailure { error ->
                            _state.update { it.copy(isLoading = false) }

                            error.onLocalFailure {
                                if (it.localError is AuthExceptions) {
                                    val authExceptions = it.localError as AuthExceptions
                                    val localError = authExceptions.fromException()
                                    sendError(ApiResult.Error.Local(localError))
                                    return@onFailure
                                }
                            }

                            sendError(error)
                        }
                }
            }

            is LoginEvent.OnRememberMeValueChange -> {
                _state.update {
                    it.copy(
                        isRememberMe = event.value
                    )
                }
            }
        }
    }

    override fun mapBaseError(message: String): LoginAction {
        return LoginAction.OnError(message)
    }
}