package com.foodsaver.app.coreCategory.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreCategory.domain.model.CategoryModel

interface CategoryRepository {

    suspend fun getAllCategories(): ApiResult<List<CategoryModel>>
}