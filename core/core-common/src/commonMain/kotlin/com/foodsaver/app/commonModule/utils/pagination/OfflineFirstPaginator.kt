package com.foodsaver.app.commonModule.utils.pagination

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess

class OfflineFirstPaginator<Key, Item>(
    initialKey: Key,
    private val onCacheRequest: suspend () -> ApiResult<Item>,
    onNetworkRequest: suspend (nextKey: Key) -> ApiResult<Item>,
    onSuccess: suspend (nextKey: Key, result: Item) -> Unit,
    onError: suspend (error: ApiResult.Error?) -> Unit,
    onNextKey: (currentKey: Key, result: Item) -> Key,
    onLoadUpdated: (Boolean) -> Unit,
    onEndReaching: (currentKey: Key, result: Item) -> Boolean,
) : BasePaginator<Key, Item>(
    initialKey,
    onRequest = onNetworkRequest,
    onSuccess = onSuccess,
    onError = onError,
    onNextKey = onNextKey,
    onLoadUpdated = onLoadUpdated,
    onEndReaching = onEndReaching
) {

    private var isCacheLoaded = false

    override suspend fun loadPage() {
        onLoadUpdated(true)

        if (!isCacheLoaded) {
            // load cache in first
            val cache = onCacheRequest()
            cache.onSuccess { result ->
                println("Cache pagination $result")
                onSuccess(currentKey, result)
            }
            isCacheLoaded = true
            onLoadUpdated(false)
        }

        super.loadPage()
    }

    suspend fun loadPage(
        isSearching: Boolean,
        onSearchRequest: suspend (nextKey: Key) -> ApiResult<Item>
    ) {
        super.reset()
        onLoadUpdated(true)

        if (isSearching) {
            val search = onSearchRequest.invoke(currentKey)
            search.onSuccess { result ->
                onSuccess(currentKey, result)
                currentKey = onNextKey(currentKey, result)
                isEndReaching = onEndReaching(currentKey, result)
            }.onFailure { error ->
                onError(error)
            }
            onLoadUpdated(false)
        }
    }

    override fun reset() {
        super.reset()
        isCacheLoaded = false
    }
}