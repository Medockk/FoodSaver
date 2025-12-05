package com.foodsaver.app.presentation

import com.foodsaver.app.utils.Paginator

class PaginatorExample {

    private val pageSize = 10
    val paginator = Paginator<Int, Response>(
        initKey = 0,
        onLoadUpdated = { isLoading -> },
        onRequest = { currentKey -> TODO("Должно вернуть ApiResult<Item>") },
        onNextKey = { currentKey, _ -> currentKey+1 },
        onError = {  },
        onSuccess = { nextKey, result -> },
        endReached = { currentKey, result -> (currentKey * pageSize) >= result.data.size }
    )
}

data class Response(val data: List<Any>)