package com.foodsaver.app.di

fun initComposeAppKoin() = initSharedKoin(
    modules = arrayOf(uiModule)
)