package com.foodsaver.app

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val Dispatchers.InputOutput: CoroutineContext
    get() = Dispatchers.Default