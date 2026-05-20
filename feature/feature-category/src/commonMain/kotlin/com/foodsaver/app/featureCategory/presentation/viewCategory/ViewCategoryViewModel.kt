package com.foodsaver.app.featureCategory.presentation.viewCategory

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreCategory.domain.repository.CategoryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewCategoryViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel<ViewCategoryAction>() {

    private val _state = MutableStateFlow(ViewCategoryState())
    val state = _state.asStateFlow()

    init {
        fetchCategories()
    }

    private fun fetchCategories(): Job {
        return viewModelScope.launch {
            categoryRepository.fetchAllCategories().onSuccess { categories ->
                _state.update {
                    it.copy(
                        allCategories = categories
                    )
                }
            }
        }
    }

    fun onRefresh() {
        _state.update { it.copy(isRefreshing = true) }
        fetchCategories().invokeOnCompletion {
            viewModelScope.launch {
                delay(3000)
                _state.update { it.copy(isRefreshing = false) }
            }
        }
    }

    override fun mapBaseError(message: String): ViewCategoryAction {
        return ViewCategoryAction.OnError(message)
    }
}