package com.foodsaver.app.commonModule.utils.pagination

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess

abstract class BasePaginator<Key, Item>(
    protected val initialKey: Key,
    protected val onRequest: suspend (nextKey: Key) -> ApiResult<Item>,
    protected val onSuccess: suspend (nextKey: Key, result: Item) -> Unit,
    protected val onError: suspend (error: ApiResult.Error) -> Unit,
    protected val onNextKey: (currentKey: Key, result: Item) -> Key,
    protected val onLoadUpdated: (Boolean) -> Unit,
    protected val onEndReaching: (currentKey: Key, result: Item) -> Boolean
) {

    protected var currentKey: Key = initialKey
    protected var isMakingRequest = false
    protected var isEndReaching = false

    // load methods
    open suspend fun loadPage() {
        if (isMakingRequest || isEndReaching) {
            println("BasePaginator isMakingRequest = $isMakingRequest")
            println("BasePaginator isEndReaching = $isEndReaching")
            onLoadUpdated(false)
            return
        }

        isMakingRequest = true
        onLoadUpdated(true)

        val result = onRequest(currentKey)
        isMakingRequest = false

        result.onSuccess { result ->
            println("BasePaginator onSuccess")
            onSuccess(currentKey, result)
            currentKey = onNextKey(currentKey, result)
            isEndReaching = onEndReaching(currentKey, result)
        }.onFailure { error ->
            println("BasePaginator onFailure \n${error.uiText.asString()}")
            onError(error)
        }
        onLoadUpdated(false)
    }

    open fun reset() {
        currentKey = initialKey
        isMakingRequest = false
        isEndReaching = false
    }
}