package com.foodsaver.app.domain.repository

import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.domain.model.CategoryModel

interface CategoryRepository {

    suspend fun getAllCategories(): ApiResult<List<CategoryModel>>
}