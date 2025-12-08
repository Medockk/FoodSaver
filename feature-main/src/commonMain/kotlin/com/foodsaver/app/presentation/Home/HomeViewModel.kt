package com.foodsaver.app.presentation.Home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.domain.model.CategoryModel
import com.foodsaver.app.domain.model.ProductModel
import com.foodsaver.app.domain.usecase.GetAllCategoriesUseCase
import com.foodsaver.app.domain.usecase.GetProductsUseCase
import com.foodsaver.app.utils.ApiResult.ApiResult
import com.foodsaver.app.utils.InputOutput
import com.foodsaver.app.utils.Paginator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private val _channel = Channel<HomeAction>()
    val channel = _channel.receiveAsFlow()

    private val pageSize = 15
    private val productsPaginator = Paginator(
        initKey = 0,
        onLoadUpdated = { isLoading ->
            _state.value = state.value.copy(
                isProductsLoading = isLoading
            )
        },
        onRequest = { currentKey ->
            getProductsUseCase.invoke(currentKey, pageSize)
        },
        onNextKey = { currentKey, _ -> currentKey + 1 },
        onError = {
            _channel.send(HomeAction.OnError(it?.message ?: "Unknown error"))
        },
        onSuccess = { _, result ->
            println(result)
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    products = _state.value.products + result
                )
            }
        },
        endReached = { currentKey, result -> (currentKey * pageSize) >= result.size }
    )

    init {
        getAllCategories()
        loadProducts()
    }

    fun getAllCategories() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            when (val result = getAllCategoriesUseCase.invoke()) {
                is ApiResult.Error -> {
                    _state.value = state.value.copy(isLoading = false)
                    _channel.send(HomeAction.OnError(result.error.message))
                }

                ApiResult.Loading -> Unit
                is ApiResult.Success<List<CategoryModel>> -> {
                    println(result.data)
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(
                            isLoading = false,
                            categories = result.data
                        )
                    }
                }
            }
        }
    }

    fun loadProducts() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            productsPaginator.loadPage()
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnCategoryIndexChange -> {
                _state.value = state.value.copy(
                    categoryIndex = event.value
                )
            }

            is HomeEvent.OnSearchQueryChange -> {
                _state.value = state.value.copy(
                    searchQuery = event.value
                )
            }

            HomeEvent.LoadNextProducts -> {
                loadProducts()
            }
        }
    }
}