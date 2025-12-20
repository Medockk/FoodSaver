package com.foodsaver.app.di

import com.foodsaver.app.data.repository.LogoutRepositoryImpl
import com.foodsaver.app.data.repository.ProfilePersonalInfoRepositoryImpl
import com.foodsaver.app.domain.repository.LogoutRepository
import com.foodsaver.app.domain.repository.ProfilePersonalInfoRepository
import com.foodsaver.app.domain.usecase.auth.LogoutUseCase
import com.foodsaver.app.domain.usecase.personalInfo.SavePersonalInfoUseCase
import com.foodsaver.app.presentation.ProfileMenu.ProfileViewModel
import com.foodsaver.app.presentation.ProfilePersonalInfo.ProfilePersonalInfoViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureProfileModule = module {

    single<LogoutRepository> {
        LogoutRepositoryImpl(
            httpClient = get(),
            provider = get()
        )
    }
    single<ProfilePersonalInfoRepository> {
        ProfilePersonalInfoRepositoryImpl(
            httpClient = get(),
            provider = get()
        )
    }

    factoryOf(::LogoutUseCase)
    factoryOf(::SavePersonalInfoUseCase)

    viewModelOf(::ProfileViewModel)
    viewModelOf(::ProfilePersonalInfoViewModel)
}