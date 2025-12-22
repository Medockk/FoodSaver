package com.foodsaver.app.di

import com.foodsaver.app.AppViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformUiModule: Module
val uiModule: Module = module {
    includes(platformUiModule)
    viewModelOf(::AppViewModel)
}