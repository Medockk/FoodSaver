package com.foodsaver.app.commonModule


import kotlin.coroutines.CoroutineContext

actual val kotlinx.coroutines.Dispatchers.InputOutput: CoroutineContext
get() = kotlinx.coroutines.Dispatchers.IO