package com.foodsaver.app.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val Dispatchers.InputOutput: CoroutineDispatcher
    get() = Dispatchers.Default