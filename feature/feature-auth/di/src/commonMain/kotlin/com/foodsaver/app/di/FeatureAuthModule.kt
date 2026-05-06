package com.foodsaver.app.di

import com.foodsaver.app.data.repository.AuthRepositoryImpl
import com.foodsaver.app.data.repository.GoogleAuthenticator
import com.foodsaver.app.domain.repository.AuthRepository
import com.foodsaver.app.domain.usecase.AuthenticateWithGoogleUseCase
import com.foodsaver.app.domain.usecase.ForgotPasswordUseCase
import com.foodsaver.app.domain.usecase.ResetPasswordUseCase
import com.foodsaver.app.domain.usecase.SignInUseCase
import com.foodsaver.app.domain.usecase.SignUpUseCase
import com.foodsaver.app.feature.auth.presentation.ForgotPassword.ForgotPasswordViewModel
import com.foodsaver.app.feature.auth.presentation.ResetPassword.ResetPasswordViewModel
import com.foodsaver.app.feature.auth.presentation.login.LoginViewModel
import com.foodsaver.app.feature.auth.presentation.signup.SignupViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal expect val featurePlatformModule: Module
private val module = module {

    includes(featurePlatformModule)

    single<AuthRepository> {
        AuthRepositoryImpl(
            httpClient = get(),
            accessTokenManager = get(),
            googleAuthenticator = get<GoogleAuthenticator>()
        )
    }

    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { AuthenticateWithGoogleUseCase(get()) }
    factoryOf(::ForgotPasswordUseCase)
    factoryOf(::ResetPasswordUseCase)

    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::ForgotPasswordViewModel)

    // TODO remove
    viewModelOf(::ResetPasswordViewModel)
}
val featureAuthModule = arrayOf(
    featurePlatformModule,
    module
)