package com.foodsaver.app.di

import com.foodsaver.app.data.repository.LogoutRepositoryImpl
import com.foodsaver.app.domain.repository.LogoutRepository
import com.foodsaver.app.domain.usecase.auth.LogoutUseCase
import com.foodsaver.app.presentation.addAddress.ProfileAddAddressViewModel
import com.foodsaver.app.presentation.profileAddress.ProfileAddressViewModel
import com.foodsaver.app.presentation.profileEditProfile.ProfileEditProfileViewModel
import com.foodsaver.app.presentation.profileMenu.ProfileMenuViewModel
import com.foodsaver.app.presentation.profilePersonalInfo.ProfilePersonalInfoViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureProfileModule = module {

    single<LogoutRepository> {
        LogoutRepositoryImpl(
            databaseProvider = get(),
            accessTokenManager = get(),
            authUserManager = get(),
        )
    }

    factoryOf(::LogoutUseCase)

    viewModelOf(::ProfileMenuViewModel)
    viewModelOf(::ProfilePersonalInfoViewModel)
    viewModelOf(::ProfileAddressViewModel)
    viewModelOf(::ProfileEditProfileViewModel)
    viewModelOf(::ProfileAddAddressViewModel)
}