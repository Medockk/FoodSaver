package com.foodsaver.app.domain.repository

import com.foodsaver.app.domain.model.CategoryModel
import com.foodsaver.app.utils.ApiResult.ApiResult

interface CategoryRepository {

    suspend fun getAllCategories(): ApiResult<List<CategoryModel>>
}