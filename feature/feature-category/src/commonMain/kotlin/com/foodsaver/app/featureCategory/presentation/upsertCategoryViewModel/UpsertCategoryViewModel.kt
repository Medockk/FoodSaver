package com.foodsaver.app.featureCategory.presentation.upsertCategoryViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreCategory.domain.model.AddCategoryRequest
import com.foodsaver.app.coreCategory.domain.model.UpdateCategoryRequest
import com.foodsaver.app.coreCategory.domain.repository.CategoryRepository
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpsertCategoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository
): BaseViewModel<UpsertCategoryAction>() {

    private val _state = MutableStateFlow(UpsertCategoryState())
    val state = _state.asStateFlow()
    val navArgs = savedStateHandle.toRoute<Route.AdminGraph.UpsertCategoryScreen>()

    init {
        navArgs.categoryId?.let { id ->
            fetchCategory(id)
        }
    }

    private fun fetchCategory(categoryId: String) {
        viewModelScope.launch {
            categoryRepository.fetchCategoryById(categoryId).onSuccess { category ->
                _state.update { it.copy(
                    category = category
                ) }
            }
        }
    }

    fun onEvent(event: UpsertCategoryEvent) {
        when (event) {
            is UpsertCategoryEvent.OnIsDeletedChange -> {
                val category = _state.value.category ?: return
                viewModelScope.launch {
                    categoryRepository.updateCategory(
                        request = UpdateCategoryRequest(category.categoryId, isDeleted = true)
                    ).onSuccess {
                        baseChannel.send(UpsertCategoryAction.OnCategoryUpserted)
                    }
                }
            }
            is UpsertCategoryEvent.OnNameChange -> {
                _state.update { it.copy(
                    category = it.category?.copy(categoryName = event.value),
                    name = event.value
                ) }
            }
            UpsertCategoryEvent.OnSave -> {
                val currentState = _state.value
                with(currentState) {
                    if (category != null) {
                        // update category
                        val request = UpdateCategoryRequest(
                            id = category.categoryId,
                            name = category.categoryName,
                            isDeleted = isDeleted
                        )
                        viewModelScope.launch {
                            categoryRepository.updateCategory(request).onSuccess {
                                baseChannel.send(UpsertCategoryAction.OnCategoryUpserted)
                            }
                        }
                    } else {
                        // add category
                        val request = AddCategoryRequest(name)
                        viewModelScope.launch {
                            categoryRepository.addCategory(request).onSuccess {
                                baseChannel.send(UpsertCategoryAction.OnCategoryUpserted)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun mapBaseError(message: String): UpsertCategoryAction {
        return UpsertCategoryAction.OnError(message)
    }
}