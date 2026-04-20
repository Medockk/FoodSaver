package com.foodsaver.app.commonModule.utils.pagination

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.getOrElse

class DefaultPaginator<Key, Item>(
    initialKey: Key,
    onLoadUpdated: (Boolean) -> Unit,
    onRequest: suspend (nextKey: Key) -> ApiResult<Item>,
    onNextKey: (currentKey: Key, result: Item) -> Key,
    onError: suspend (ApiResult.Error?) -> Unit,
    onSuccess: suspend (nextKey: Key, result: Item) -> Unit,
    onEndReaching: (currentKey: Key, result: Item) -> Boolean
): BasePaginator<Key, Item>(
    initialKey,
    onRequest,
    onSuccess,
    onError,
    onNextKey,
    onLoadUpdated,
    onEndReaching
)