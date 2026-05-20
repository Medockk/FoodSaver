package com.foodsaver.app.featureCategory.di

import com.foodsaver.app.featureCategory.presentation.upsertCategoryViewModel.UpsertCategoryViewModel
import com.foodsaver.app.featureCategory.presentation.viewCategory.ViewCategoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureCategoryModule = module {

    viewModelOf(::ViewCategoryViewModel)
    viewModelOf(::UpsertCategoryViewModel)
}