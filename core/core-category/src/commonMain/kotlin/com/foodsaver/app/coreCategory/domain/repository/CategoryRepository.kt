package com.foodsaver.app.coreCategory.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreCategory.domain.model.AddCategoryRequest
import com.foodsaver.app.coreCategory.domain.model.CategoryModel
import com.foodsaver.app.coreCategory.domain.model.UpdateCategoryRequest

interface CategoryRepository {

    suspend fun fetchAllCategories(): ApiResult<List<CategoryModel>>
    suspend fun fetchCategoryById(id: String): ApiResult<CategoryModel>

    suspend fun addCategory(request: AddCategoryRequest): ApiResult<CategoryModel>
    suspend fun updateCategory(request: UpdateCategoryRequest): ApiResult<CategoryModel>
}