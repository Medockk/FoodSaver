package com.foodsaver.app.utils

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val Dispatchers.InputOutput: CoroutineContext
    get() = Default