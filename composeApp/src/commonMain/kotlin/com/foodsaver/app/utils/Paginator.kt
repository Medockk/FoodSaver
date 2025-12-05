package com.foodsaver.app.utils

import com.foodsaver.app.dto.GlobalErrorResponse
import com.foodsaver.app.utils.ApiResult.ApiResult
import com.foodsaver.app.utils.ApiResult.getOrElse

class Paginator<Key, Item>(
    initKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> ApiResult<Item>,
    private val onNextKey: (currentKey: Key, result: Item) -> Key,
    private val onError: (GlobalErrorResponse?) -> Unit,
    private val onSuccess: suspend (nextKey: Key, result: Item) -> Unit,
    private val endReached: (currentKey: Key, result: Item) -> Boolean
) {

    private var currentKey = initKey
    private var isMakingRequest = false
    private var isEndReached = false

    suspend fun loadPage() {
        if (isMakingRequest || isEndReached) return

        isMakingRequest = true
        onLoadUpdated(true)

        val result = onRequest(currentKey)
        isMakingRequest = false

        val item = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }

        currentKey = onNextKey(currentKey, item)
        onSuccess(currentKey, item)
        onLoadUpdated(false)

        isEndReached = endReached(currentKey, item)
    }
}