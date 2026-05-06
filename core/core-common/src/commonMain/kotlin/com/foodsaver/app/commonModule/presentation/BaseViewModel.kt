package com.foodsaver.app.commonModule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.apiResult.ApiResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock

abstract class BaseViewModel<A: AppAction>: ViewModel() {

    private var lastErrorMessage: String = ""
    private var lastErrorTimeMs: Long = 0L
    private val errorBounceMs = 5000L

    protected open val baseChannel: Channel<A> = Channel()
    open val channel: Flow<A> = baseChannel.receiveAsFlow()

    protected abstract fun mapBaseError(message: String): A

    protected suspend fun <T> Flow<ApiResult<T>>.collectRequest(
        onSuccess: suspend (T) -> Unit,
        onLoading: (() -> Unit)? = null,
        onError: (suspend (ApiResult.Error) -> Unit)? = null
    ) {
        this.collect { result ->
            when (result) {
                is ApiResult.Error -> {
                    onError?.invoke(result) ?: sendError(result.uiText.asString())
                }
                ApiResult.Loading -> onLoading?.invoke()
                is ApiResult.Success<T> -> onSuccess(result.data)
                ApiResult.Idle -> Unit
            }
        }
    }

    protected fun sendError(message: String) {
        viewModelScope.launch {
            receiveError(message, onSuspendSend = {
                baseChannel.send(mapBaseError(message))
            })
        }

    }

    protected fun trySendError(message: String) {
        receiveError(message, onSend =  {
            baseChannel.trySend(mapBaseError(it))
        })
    }

    protected fun trySendError(error: ApiResult.Error) {
        viewModelScope.launch {
            receiveError(error.uiText.asString(), onSend =  {
                baseChannel.trySend(mapBaseError(it))
            })
        }
    }

    protected fun sendError(error: ApiResult.Error) {
        viewModelScope.launch {
            receiveError(message = error.uiText.asString(), onSuspendSend = {
                baseChannel.send(mapBaseError(it))
            })
        }
    }

    private fun receiveError(message: String, onSend: (message: String) -> Unit) {
        val currentTimeMs = Clock.System.now().toEpochMilliseconds()

        if (lastErrorMessage == message && (currentTimeMs - lastErrorTimeMs) < errorBounceMs) {
            return
        }

        lastErrorMessage = message
        lastErrorTimeMs = currentTimeMs

        onSend(message)
    }

    private suspend fun receiveError(message: String, onSuspendSend: suspend (message: String) -> Unit) {
        val currentTimeMs = Clock.System.now().toEpochMilliseconds()

        if (lastErrorMessage == message && (currentTimeMs - lastErrorTimeMs) < errorBounceMs) {
            return
        }

        lastErrorMessage = message
        lastErrorTimeMs = currentTimeMs

        onSuspendSend(message)
    }
}