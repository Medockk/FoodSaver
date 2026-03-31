package com.foodsaver.app.coreFcm.di

import com.foodsaver.app.coreFcm.service.FcmService
import com.foodsaver.app.coreFcm.service.FcmServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformCoreFcmModule: Module
    get() = module {
        single<FcmService> {
            FcmServiceImpl()
        }
    }