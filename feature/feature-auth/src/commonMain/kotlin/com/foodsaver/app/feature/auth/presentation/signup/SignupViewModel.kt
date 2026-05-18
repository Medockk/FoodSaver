package com.foodsaver.app.feature.auth.presentation.signup

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreFcm.service.FcmService
import com.foodsaver.app.domain.model.SignUpModel
import com.foodsaver.app.domain.usecase.SignUpUseCase
import com.foodsaver.app.domain.utils.EmailValidator
import com.foodsaver.app.feature.auth.common.AuthLocalError
import com.foodsaver.app.feature.auth.presentation.utils.AuthenticationBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
    fcmService: FcmService,
    authManager: AuthUserManager,
    private val signUpUseCase: SignUpUseCase
): AuthenticationBaseViewModel<SignupAction>(fcmService, authManager) {

    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()

    fun onEvent(event: SignupEvent) {
        when (event) {
            SignupEvent.ChangePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            SignupEvent.ChangeRetypePasswordVisibility -> {
                _state.update { it.copy(isRetypePasswordVisible = !it.isRetypePasswordVisible) }
            }
            is SignupEvent.OnEmailValueChange -> {
                _state.update { it.copy(email = event.value) }
            }
            is SignupEvent.OnNameValueChange -> {
                _state.update { it.copy(name = event.value) }
            }
            is SignupEvent.OnPasswordValueChange -> {
                _state.update { it.copy(password = event.value) }
            }
            is SignupEvent.OnRetypePasswordValueChange -> {
                _state.update { it.copy(retypePassword = event.value) }
            }
            SignupEvent.Signup -> {

                val currentState = _state.value

                if (currentState.email.isBlank()) {
                    val error = AuthLocalError.EmptyEmail
                    sendError(ApiResult.Error.Local(error))
                    return
                }
                if (!EmailValidator.validate(currentState.email)) {
                    val error = AuthLocalError.InvalidEmail
                    sendError(ApiResult.Error.Local(error))
                    return
                }
                if (currentState.name.isBlank()) {
                    val error = AuthLocalError.EmptyFio
                    sendError(ApiResult.Error.Local(error))
                    return
                }
                if (currentState.password.isBlank()) {
                    val error = AuthLocalError.EmptyPassword
                    sendError(ApiResult.Error.Local(error))
                    return
                }
                if (currentState.password != currentState.retypePassword) {
                    val error = AuthLocalError.PasswordNotEqual
                    sendError(ApiResult.Error.Local(error))
                    return
                }

                val isFieldsNotEmpty = checkFields(
                    fields = listOf(
                        _state.value.email,
                        _state.value.name,
                        _state.value.password,
                        _state.value.retypePassword,
                    )
                )

                if (isFieldsNotEmpty && EmailValidator.validate(_state.value.email)) {
                    _state.update { it.copy(isLoading = true) }
                    viewModelScope.launch(Dispatchers.InputOutput) {
                        val requestModel = SignUpModel(
                            email = _state.value.email,
                            password = _state.value.password,
                            fullName = _state.value.name
                        )

                        signUpUseCase(requestModel)
                            .onSuccess { responseModel ->
                                onSaveFcmToken()
                                saveAuthenticationSession(responseModel.uid)
                                _state.update { it.copy(isLoading = false) }
                                baseChannel.send(SignupAction.OnRegistered)
                            }.onFailure { error ->
                                _state.update { it.copy(isLoading = false) }
                                sendError(error)
                            }
                    }
                } else {
                    trySendError("Something empty")
                }
            }
        }
    }

    override fun mapBaseError(message: String): SignupAction {
        return SignupAction.OnError(message)
    }
}