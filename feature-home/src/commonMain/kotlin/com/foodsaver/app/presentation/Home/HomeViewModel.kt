@file:OptIn(FlowPreview::class)

package com.foodsaver.app.presentation.Home

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.onFailure
import com.foodsaver.app.commonModule.ApiResult.onSuccess
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreAddress.domain.repository.ReadAddressRepository
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.usecase.AddProductToCartUseCase
import com.foodsaver.app.domain.usecase.GetAllCategoriesUseCase
import com.foodsaver.app.domain.usecase.GetCartUseCase
import com.foodsaver.app.domain.usecase.GetProductsUseCase
import com.foodsaver.app.coreProfile.domain.usecase.GetProfileUseCase
import com.foodsaver.app.domain.usecase.RemoveProductFromCartUseCase
import com.foodsaver.app.domain.usecase.SearchProductUseCase
import com.foodsaver.app.domain.usecase.offer.GetOffersUseCase
import com.foodsaver.app.presentation.Home.HomeAction.OnError
import com.foodsaver.app.utils.Paginator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val getOffersUseCase: GetOffersUseCase,
    private val readAddressRepository: ReadAddressRepository
) : BaseViewModel<HomeAction>() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    override val baseChannel: Channel<HomeAction> = Channel()

    val channel = baseChannel.receiveAsFlow()

    private var searchJob: Job? = null

    private val pageSize = 8

    private val productsPaginator = Paginator(
        initKey = 0,
        onLoadUpdated = { isLoading ->
            _state.update { it.copy(isProductsLoading = isLoading) }
        },
        onRequest = { currentKey ->
            getProductsUseCase.invoke(currentKey, pageSize)
        },
        onNextKey = { currentKey, _ -> currentKey + 1 },
        onError = { errorResponse ->
            sendError(errorResponse?.message ?: "Unknown error")
        },
        onSuccess = { _, result ->
            _state.update {
                it.copy(
                    products = _state.value.products + result,
                    isProductsLoading = false
                )
            }
        },
        endReached = { _, result -> pageSize > result.size }
    )
    private val searchPaginator = Paginator(
        initKey = 0,
        onLoadUpdated = { isLoading ->
            _state.update { it.copy(isProductsLoading = isLoading) }
        },
        onRequest = { page ->
            searchProductUseCase.invoke(
                productName = _state.value.searchQuery,
                categoryIds = _state.value.selectedCategoryIds.toList(),
                page = page,
                size = pageSize
            )
        },
        onNextKey = { currentKey, _ -> currentKey + 1 },
        onError = { errorResponse ->
            sendError(errorResponse?.message ?: "Unknown error")
        },
        onSuccess = { key, result ->
            println("key is $key")
            if (key == 0) {
                _state.update {
                    it.copy(
                        searchedProducts = result,
                        productsDisplayMode = ProductsDisplayMode.Searched,
                        isProductsLoading = false
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
                        productsDisplayMode = ProductsDisplayMode.Searched,
                        isProductsLoading = false
                    )
                }
            }
        },
        endReached = { currentKey, result -> (currentKey * pageSize) >= result.size }
    )

    init {
        getOffers()
        loadProducts()
        loadCart()
        getAllCategories()
        getProfile()
        getCurrentAddress()
    }

    private fun getCurrentAddress() = viewModelScope.launch(Dispatchers.InputOutput) {
        readAddressRepository.getCurrentAddress().collectRequest(
            onSuccess = { address ->
                _state.update { it.copy(currentAddress = address) }
                println("Current address ${_state.value.currentAddress}")
            }
        )
    }

    private fun getOffers() = viewModelScope.launch(Dispatchers.InputOutput) {
        _state.update { it.copy(isOffersLoading = true) }
        getOffersUseCase.invoke()
            .onSuccess { response ->
                _state.update { it.copy(offers = response, isOffersLoading = false) }
            }
    }

    fun onRefresh() {
        _state.update { it.copy(isRefresh = true) }
        viewModelScope.launch(Dispatchers.InputOutput) {
            arrayOf(
                loadProducts(),
                loadCart(),
                getAllCategories(),
                getProfile(),
                getCurrentAddress(),
            )

            delay(1000)
            withContext(Dispatchers.Main) {
                _state.update { it.copy(isRefresh = false) }
            }
        }
    }

    private fun getProfile(): Job {
        return viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase().collectRequest(
                onSuccess = { result ->
                    _state.update { it.copy(profile = result) }
                }
            )
        }
    }

    private fun getAllCategories(): Job {
        return viewModelScope.launch(Dispatchers.InputOutput) {
            _state.update { it.copy(isCategoriesLoading = true) }
            getAllCategoriesUseCase().onSuccess { result ->
                _state.update { it.copy(categories = result, isCategoriesLoading = false) }
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
            getCartUseCase().collectRequest(
                onSuccess = { result ->
                    val cartIds = result.map { item -> item.product.productId }.toSet()
                    _state.update {
                        it.copy(
                            cartProducts = result,
                            cartProductIds = cartIds,
                        )
                    }
                }
            )
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

                if (_state.value.selectedCategoryIds.isEmpty() && _state.value.searchQuery.isBlank()) {
                    _state.update {
                        it.copy(productsDisplayMode = ProductsDisplayMode.All)
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

                if (_state.value.searchQuery.isBlank() && _state.value.selectedCategoryIds.isEmpty()) {
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
                            baseChannel.send(OnError(result.error.message))
                        }
                    }
                } else {
                    viewModelScope.launch(Dispatchers.InputOutput) {
                        val request = CartRequestModel(event.productId)
                        when (val result = addProductToCartUseCase.invoke(request)) {
                            is ApiResult.Error -> {
                                baseChannel.send(OnError(result.error.message))
                            }

                            ApiResult.Loading -> Unit
                            is ApiResult.Success<CartItemModel> -> Unit
                        }
                    }
                }
            }

            is HomeEvent.OnOfferClick -> {
                val cart = _state.value.cartProducts
                    .find { it.product.productId == event.productId }
                val isProductInCart = cart != null

                val actionElement =
                    HomeAction.OnProductNavigation(event.productId, isProductInCart, cart?.quantity)
                baseChannel.trySend(actionElement)
                    .onFailure {
                        viewModelScope.launch { baseChannel.send(actionElement) }
                    }
            }

            is HomeEvent.OnProductClick -> {
                val cartItem = _state.value.cartProducts
                    .find { it.product.productId == event.productId }

                baseChannel.trySend(HomeAction.OnProductNavigation(
                    productId = event.productId,
                    isProductInCart = cartItem != null,
                    cartProductCount = cartItem?.quantity
                ))
            }
        }
    }

    override fun mapBaseError(message: String): HomeAction {
        return OnError(message)
    }
}