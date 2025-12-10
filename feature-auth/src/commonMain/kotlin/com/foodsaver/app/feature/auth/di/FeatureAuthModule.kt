package com.foodsaver.app.feature.auth.di

import com.foodsaver.app.client.HttpClientType
import com.foodsaver.app.feature.auth.data.repository.AuthRepositoryImpl
import com.foodsaver.app.feature.auth.domain.repository.AuthRepository
import com.foodsaver.app.feature.auth.domain.usecase.AuthenticateWithGoogleUseCase
import com.foodsaver.app.feature.auth.domain.usecase.SignInUseCase
import com.foodsaver.app.feature.auth.domain.usecase.SignUpUseCase
import com.foodsaver.app.feature.auth.presentation.Auth.AuthViewModel
import com.foodsaver.app.feature.auth.presentation.SignIn.SignInViewModel
import com.foodsaver.app.feature.auth.presentation.SignUp.SignUpViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal expect val platformModule: Module
private val module = module {
    single<AuthRepository> {
        AuthRepositoryImpl(
            httpClient = get(named(HttpClientType.MAIN_CLIENT)),
            accessTokenManager = get(),
            insertUserUseCase = get(),
            getAllUsersUseCase = get(),
            getUserByUidUseCase = get(),
        )
    }

    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { AuthenticateWithGoogleUseCase(get()) }

    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::AuthViewModel)
}
val featureAuthModule = arrayOf(
    module,
    platformModule
)