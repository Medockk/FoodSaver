package com.foodsaver.app.featureSearch.di

import com.foodsaver.app.featureSearch.data.repository.SearchRepositoryImpl
import com.foodsaver.app.featureSearch.domain.repository.SearchRepository
import com.foodsaver.app.featureSearch.presentation.search.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val featureSearchModule = module {

    single<SearchRepositoryImpl>() bind SearchRepository::class
    viewModelOf(::SearchViewModel)
}