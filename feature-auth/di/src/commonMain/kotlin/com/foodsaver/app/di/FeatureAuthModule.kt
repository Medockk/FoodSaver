package com.foodsaver.app.di

import com.foodsaver.app.data.repository.AuthRepositoryImpl
import com.foodsaver.app.data.repository.GoogleAuthenticator
import com.foodsaver.app.domain.repository.AuthRepository
import com.foodsaver.app.domain.usecase.AuthenticateWithGoogleUseCase
import com.foodsaver.app.domain.usecase.IsUserLoginUseCase
import com.foodsaver.app.domain.usecase.SignInUseCase
import com.foodsaver.app.domain.usecase.SignUpUseCase
import com.foodsaver.app.feature.auth.presentation.Auth.AuthViewModel
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
            googleAuthenticator = get<GoogleAuthenticator>(),
            databaseProvider = get()
        )
    }

    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { AuthenticateWithGoogleUseCase(get()) }
    factoryOf(::IsUserLoginUseCase)

    viewModelOf(::AuthViewModel)
}
val featureAuthModule = arrayOf(
    featurePlatformModule,
    module
)