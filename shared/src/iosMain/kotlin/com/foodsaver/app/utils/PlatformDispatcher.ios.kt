package com.foodsaver.app.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual val Dispatchers.InputOutput: CoroutineDispatcher
    get() = Dispatchers.IO