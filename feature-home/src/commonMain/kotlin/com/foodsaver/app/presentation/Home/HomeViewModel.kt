package com.foodsaver.app.presentation.Home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.InputOutput
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.model.CategoryModel
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.usecase.AddProductToCartUseCase
import com.foodsaver.app.domain.usecase.GetAllCategoriesUseCase
import com.foodsaver.app.domain.usecase.GetCartUseCase
import com.foodsaver.app.domain.usecase.GetProductsUseCase
import com.foodsaver.app.domain.usecase.GetProfileUseCase
import com.foodsaver.app.utils.Paginator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getCartUseCase: GetCartUseCase,

    private val addProductToCartUseCase: AddProductToCartUseCase,

    private val getProfileUseCase: GetProfileUseCase
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
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(
                    products = _state.value.products + result
                )
            }
        },
        endReached = { currentKey, result -> (currentKey * pageSize) >= result.size }
    )

    init {
        loadProducts()
        loadCart()
        getAllCategories()
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase().collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _channel.send(HomeAction.OnError(result.error.message))
                    }
                    ApiResult.Loading -> Unit
                    is ApiResult.Success<UserModel> -> {
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(
                                profile = result.data
                            )
                        }
                    }
                }
            }
        }
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

    private fun loadProducts() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            productsPaginator.loadPage()
        }
    }

    private fun loadCart() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getCartUseCase().collect {
                when (it) {
                    is ApiResult.Error -> {
                        println(it.error)
                    }
                    ApiResult.Loading -> Unit
                    is ApiResult.Success<List<CartItemModel>> -> {
                        withContext(Dispatchers.Main) {
                            println("Cart is ${it.data}")
                            _state.value = state.value.copy(
                                cartProducts = it.data
                            )
                        }
                    }
                }
            }
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

            is HomeEvent.OnAddProductToCart -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    val request = CartRequestModel(productId = event.productId)
                    when (val result = addProductToCartUseCase.invoke(request)) {
                        is ApiResult.Error -> {
                            _channel.send(HomeAction.OnError(result.error.message))
                        }
                        ApiResult.Loading -> Unit
                        is ApiResult.Success<CartItemModel> -> {
                            val cartProducts = _state.value.cartProducts + result.data
                            _state.value = state.value.copy(
                                cartProducts = cartProducts
                            )
                        }
                    }
                }
            }
        }
    }
}