package com.foodsaver.app.commonModule.presentation

import androidx.lifecycle.ViewModel
import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.dto.GlobalErrorResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.time.Clock

abstract class BaseViewModel<A: AppAction>: ViewModel() {

    private var lastErrorMessage: String = ""
    private var lastErrorTimeMs: Long = 0L
    private val errorBounceMs = 5000L

    protected abstract val baseChannel: Channel<A>
    abstract val channel: Flow<A>

    protected abstract fun mapBaseError(message: String): A

    protected suspend fun <T> Flow<ApiResult<T>>.collectRequest(
        onSuccess: suspend (T) -> Unit,
        onLoading: (() -> Unit)? = null,
        onError: (suspend (GlobalErrorResponse) -> Unit)? = null
    ) {
        this.collect { result ->
            when (result) {
                is ApiResult.Error -> {
                    onError?.invoke(result.error) ?: sendError(result.error.message)
                }
                ApiResult.Loading -> onLoading?.invoke()
                is ApiResult.Success<T> -> onSuccess(result.data)
            }
        }
    }

    protected suspend fun sendError(message: String) {
        val currentTimeMs = Clock.System.now().toEpochMilliseconds()

        if (lastErrorMessage == message && (currentTimeMs - lastErrorTimeMs) < errorBounceMs) {
            return
        }

        lastErrorMessage = message
        lastErrorTimeMs = currentTimeMs

        baseChannel.send(mapBaseError(message))
    }

    protected fun trySendError(message: String) {
        val currentTimeMs = Clock.System.now().toEpochMilliseconds()

        if (lastErrorMessage == message && (currentTimeMs - lastErrorTimeMs) < errorBounceMs) {
            return
        }

        lastErrorMessage = message
        lastErrorTimeMs = currentTimeMs

        baseChannel.trySend(mapBaseError(message))
    }
}