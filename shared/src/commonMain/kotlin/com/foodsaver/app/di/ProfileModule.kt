package com.foodsaver.app.di

import com.foodsaver.app.data.repository.ProfileRepositoryImpl
import com.foodsaver.app.di.utils.HttpClientType
import com.foodsaver.app.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileModule = module {

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            client = get<HttpClient>(named(HttpClientType.MAIN_CLIENT))
        )
    }
}