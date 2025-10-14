package com.foodsaver.app.di

import com.foodsaver.app.data.repository.AuthRepositoryImpl
import com.foodsaver.app.di.utils.HttpClientType
import com.foodsaver.app.domain.repository.AuthRepository
import com.foodsaver.app.domain.usecase.Auth.SignInUseCase
import com.foodsaver.app.domain.usecase.Auth.SignUpUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {

    single<AuthRepository> {
        AuthRepositoryImpl(
            client = get(named(HttpClientType.MAIN_CLIENT)),
            tokenManager = get()
        )
    }

    factory {
        SignInUseCase(get())
    }
    factory {
        SignUpUseCase(get())
    }
}