package com.foodsaver.app.commonModule.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Page <T> (
    val content: List<T>,

    val totalElements: Long,
    val totalPages: Long,
    val size: Long,
    val numberOfElements: Long,
    val number: Long,

    val sort: Sort,
    val pageable: Pageable,


    @SerialName("first")
    val isFirst: Boolean,
    @SerialName("last")
    val isLast: Boolean,
    @SerialName("empty")
    val isEmpty: Boolean,

)

@Serializable
data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)

@Serializable
data class Pageable(
    val offset: Long,
    val sort: Sort,
    val pageNumber: Long,
    val pageSize: Long,
    @SerialName("paged")
    val isPaged: Boolean,
    @SerialName("unpaged")
    val isUnpaged: Boolean,
)
