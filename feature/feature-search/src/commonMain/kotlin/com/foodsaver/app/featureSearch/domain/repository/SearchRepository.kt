package com.foodsaver.app.featureSearch.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.featureSearch.domain.model.RecentKeywordsModel
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun search(query: String, page: Int, size: Int): ApiResult<List<ProductModel>>
    suspend fun searchByCategoryId(categoryId: String, page: Int, size: Int): ApiResult<List<ProductModel>>

    fun getRecentKeywords(): Flow<List<RecentKeywordsModel>>
    suspend fun onRecentKeywordSearch(recentKeywordId: String)
}