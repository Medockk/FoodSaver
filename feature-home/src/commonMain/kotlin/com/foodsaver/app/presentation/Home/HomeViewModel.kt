@file:OptIn(FlowPreview::class)

package com.foodsaver.app.presentation.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.utils.stateFlow
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.model.CategoryModel
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.usecase.AddProductToCartUseCase
import com.foodsaver.app.domain.usecase.GetAllCategoriesUseCase
import com.foodsaver.app.domain.usecase.GetCartUseCase
import com.foodsaver.app.domain.usecase.GetProductsUseCase
import com.foodsaver.app.domain.usecase.GetProfileUseCase
import com.foodsaver.app.domain.usecase.RemoveProductFromCartUseCase
import com.foodsaver.app.domain.usecase.SearchProductUseCase
import com.foodsaver.app.utils.Paginator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getCartUseCase: GetCartUseCase,

    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase,
    private val searchProductUseCase: SearchProductUseCase,

    private val getProfileUseCase: GetProfileUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategories = MutableStateFlow(listOf(""))

    val productsDisplayMode = combine(
        _searchQuery,
        _selectedCategories
    ) { searchQuery, selectedCategories ->
        val displayMode = when {
            searchQuery.isNotBlank() -> ProductsDisplayMode.Searched
            selectedCategories.isNotEmpty() -> ProductsDisplayMode.Searched
            else -> ProductsDisplayMode.All
        }
        displayMode
    }.stateFlow(ProductsDisplayMode.All)

    private val _channel = Channel<HomeAction>()
    val channel = _channel.receiveAsFlow()

    private var searchJob: Job? = null

    private val pageSize = 15

    init {
        collectProducts()
        loadProducts()
        loadCart()
        getAllCategories()
        getProfile()
    }

    private fun collectProducts() {
        viewModelScope.launch {
            combine(_searchQuery, _selectedCategories) {}
                .debounce(300L)
                .distinctUntilChanged()
                .collect {
                    searchPaginator.reset()
                    searchPaginator.loadPage()
                }
        }
    }

    private val productsPaginator = Paginator(
        initKey = 0,
        onLoadUpdated = { isLoading ->
            _state.update { it.copy(isProductsLoading = isLoading) }
        },
        onRequest = { currentKey ->
            getProductsUseCase.invoke(currentKey, pageSize)
        },
        onNextKey = { currentKey, _ -> currentKey + 1 },
        onError = {
            _channel.send(HomeAction.OnError(it?.message ?: "Unknown error"))
        },
        onSuccess = { _, result ->
            _state.update {
                it.copy(
                    products = _state.value.products + result
                )
            }
        },
        endReached = { currentKey, result -> (currentKey * pageSize) >= result.size }
    )
    private val searchPaginator = Paginator(
        initKey = 0,
        onLoadUpdated = { isLoading ->
            _state.update { it.copy(isProductsLoading = isLoading) }
        },
        onRequest = { page ->
            searchProductUseCase.invoke(
                _state.value.searchQuery,
                categoryIds = _state.value.selectedCategoryIds.toList(),
                page = page,
                size = pageSize
            )
        },
        onNextKey = { currentKey, _ -> currentKey + 1 },
        onError = {
            _channel.send(HomeAction.OnError(it?.message ?: "Unknown error"))
        },
        onSuccess = { key, result ->
            if (key == 0) {
                _state.update {
                    it.copy(
                        searchedProducts = result,
                        productsDisplayMode = ProductsDisplayMode.Searched
                    )
                }
            } else {
                val existingIds = _state.value.searchedProducts.map { it.productId }.toSet()
                val uniqueNewProducts = result.filter {
                    it.productId !in existingIds
                }

                _state.update {
                    it.copy(
                        searchedProducts = it.searchedProducts + uniqueNewProducts,
                        productsDisplayMode = ProductsDisplayMode.Searched
                    )
                }
            }
        },
        endReached = { currentKey, result -> (currentKey * pageSize) >= result.size }
    )

    fun onRefresh() {
        _state.update { it.copy(isRefresh = true) }
        viewModelScope.launch(Dispatchers.InputOutput) {

            val jobs = async {
                arrayOf(
                    loadProducts(),
                    loadCart(),
                    getAllCategories(),
                    getProfile(),
                )
            }

            delay(1000)
            withContext(Dispatchers.Main) {
                _state.update { it.copy(isRefresh = false) }
            }
        }
    }

    private fun getProfile(): Job {
        return viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase().collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _channel.send(HomeAction.OnError(result.error.message))
                    }

                    ApiResult.Loading -> Unit
                    is ApiResult.Success<UserModel> -> {
                        _state.update { it.copy(profile = result.data) }
                    }
                }
            }
        }
    }

    private fun getAllCategories(): Job {
        return viewModelScope.launch(Dispatchers.InputOutput) {
            when (val result = getAllCategoriesUseCase.invoke()) {
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _channel.send(HomeAction.OnError(result.error.message))
                }

                ApiResult.Loading -> Unit
                is ApiResult.Success<List<CategoryModel>> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            categories = result.data
                        )
                    }
                }
            }
        }
    }

    private fun loadProducts(): Job {
        return viewModelScope.launch(Dispatchers.InputOutput) {
            productsPaginator.loadPage()
        }
    }

    private fun loadCart(): Job {
        return viewModelScope.launch(Dispatchers.InputOutput) {
            getCartUseCase().collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        println(result.error)
                    }

                    ApiResult.Loading -> Unit
                    is ApiResult.Success<List<CartItemModel>> -> {
                        withContext(Dispatchers.Main) {
                            val cartIds = result.data.map { item -> item.product.productId }.toSet()
                            _state.update {
                                it.copy(
                                    cartProducts = result.data,
                                    cartProductIds = cartIds,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnCategoryIndexChange -> {
                val currentCategories = _state.value.selectedCategoryIds.toSet()

                _state.update { currentState ->
                    val newCategories = if (currentCategories.contains(event.value)) {
                        currentCategories - event.value
                    } else {
                        currentCategories + event.value
                    }
                    currentState.copy(selectedCategoryIds = newCategories)
                }

                searchJob?.cancel()
                searchPaginator.reset()

                if (currentCategories.contains(event.value)) {
                    // remove cayegory
                    _state.update {
                        it.copy(
                            productsDisplayMode = ProductsDisplayMode.All
                        )
                    }
                    return
                }

                searchJob = viewModelScope.launch(Dispatchers.InputOutput) {
                    searchPaginator.loadPage()
                }
            }

            is HomeEvent.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = event.value) }

                searchJob?.cancel()
                searchPaginator.reset()

                if (_state.value.searchQuery.isBlank()) {
                    _state.update { it.copy(productsDisplayMode = ProductsDisplayMode.All) }
                    return
                }

                _state.update {
                    it.copy(
                        productsDisplayMode = ProductsDisplayMode.Searched,
                        searchedProducts = it.searchedProducts.filter { filter ->
                            filter.title.contains(it.searchQuery, true)
                        }
                    )
                }
                println(_state.value.searchedProducts)
                searchJob = viewModelScope.launch(Dispatchers.InputOutput) {
                    searchPaginator.loadPage()
                }
            }

            HomeEvent.LoadNextProducts -> {
                loadProducts()
            }

            is HomeEvent.OnAddProductToCart -> {
                if (_state.value.cartProductIds.contains(event.productId)) {
                    viewModelScope.launch(Dispatchers.InputOutput) {
                        val result = removeProductFromCartUseCase(event.productId)

                        if (result is ApiResult.Error) {
                            _channel.send(HomeAction.OnError(result.error.message))
                        }
                    }
                } else {
                    viewModelScope.launch(Dispatchers.InputOutput) {
                        val request = CartRequestModel(event.productId)
                        when (val result = addProductToCartUseCase.invoke(request)) {
                            is ApiResult.Error -> {
                                _channel.send(HomeAction.OnError(result.error.message))
                            }

                            ApiResult.Loading -> Unit
                            is ApiResult.Success<CartItemModel> -> Unit
                        }
                    }
                }
            }
        }
    }
}