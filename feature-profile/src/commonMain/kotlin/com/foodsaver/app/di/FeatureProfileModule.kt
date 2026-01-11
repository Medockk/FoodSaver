package com.foodsaver.app.di

import com.foodsaver.app.data.repository.LogoutRepositoryImpl
import com.foodsaver.app.data.repository.PaymentMethodRepositoryImpl
import com.foodsaver.app.data.repository.ProfilePersonalInfoRepositoryImpl
import com.foodsaver.app.domain.repository.LogoutRepository
import com.foodsaver.app.domain.repository.PaymentMethodRepository
import com.foodsaver.app.domain.repository.ProfilePersonalInfoRepository
import com.foodsaver.app.domain.usecase.auth.LogoutUseCase
import com.foodsaver.app.domain.usecase.paymentCard.GetPaymentMethodUseCase
import com.foodsaver.app.domain.usecase.personalInfo.SavePersonalInfoUseCase
import com.foodsaver.app.domain.usecase.personalInfo.UploadAvatarUseCase
import com.foodsaver.app.presentation.ProfileAddress.ProfileAddressViewModel
import com.foodsaver.app.presentation.ProfileMenu.ProfileViewModel
import com.foodsaver.app.presentation.ProfilePaymentMethod.ProfilePaymentMethodViewModel
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

    single<PaymentMethodRepository> {
        PaymentMethodRepositoryImpl(
            httpClient = get(),
            databaseProvider = get()
        )
    }

    factoryOf(::LogoutUseCase)
    factoryOf(::SavePersonalInfoUseCase)
    factoryOf(::UploadAvatarUseCase)

    factoryOf(::GetPaymentMethodUseCase)

    viewModelOf(::ProfileViewModel)
    viewModelOf(::ProfilePersonalInfoViewModel)
    viewModelOf(::ProfileAddressViewModel)
    viewModelOf(::ProfilePaymentMethodViewModel)
}