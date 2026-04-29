package com.foodsaver.app.commonModule.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

context(scope: ViewModel)
fun <T> Flow<T>.stateFlow(initialValue: T, started: SharingStarted = SharingStarted.WhileSubscribed(5000L)) =
    this.stateIn(scope.viewModelScope, started, initialValue)