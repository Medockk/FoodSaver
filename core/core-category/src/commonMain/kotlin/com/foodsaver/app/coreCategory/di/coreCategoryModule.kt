package com.foodsaver.app.coreCategory.di

import com.foodsaver.app.coreCategory.data.repository.CategoryRepositoryImpl
import com.foodsaver.app.coreCategory.domain.repository.CategoryRepository
import org.koin.dsl.module

val coreCategoryModule = module {

    single<CategoryRepository> {
        CategoryRepositoryImpl(get())
    }
}