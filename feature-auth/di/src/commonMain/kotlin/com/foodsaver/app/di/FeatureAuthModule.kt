package com.foodsaver.app.di

import com.foodsaver.app.client.HttpClientType
import com.foodsaver.app.data.repository.AuthRepositoryImpl
import com.foodsaver.app.domain.repository.AuthRepository
import com.foodsaver.app.domain.usecase.AuthenticateWithGoogleUseCase
import com.foodsaver.app.domain.usecase.SignInUseCase
import com.foodsaver.app.domain.usecase.SignUpUseCase
import com.foodsaver.app.feature.auth.presentation.Auth.AuthViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal expect val platformModule: Module
private val module = module {
    single<AuthRepository> {
        AuthRepositoryImpl(
            httpClient = get(named(HttpClientType.MAIN_CLIENT)),
            accessTokenManager = get()
        )
    }

    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { AuthenticateWithGoogleUseCase(get()) }

    viewModelOf(::AuthViewModel)
}
val featureAuthModule = arrayOf(
    module,
    platformModule
)