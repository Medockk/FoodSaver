package com.foodsaver.app.presentation.ProductDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.InputOutput
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.usecase.AddProductToCartUseCase
import com.foodsaver.app.domain.usecase.DecreaseProductCountUseCase
import com.foodsaver.app.domain.usecase.GetCachedProductUseCase
import com.foodsaver.app.domain.usecase.IncreaseProductCountUseCase
import com.foodsaver.app.domain.usecase.RemoveProductFromCartUseCase
import com.foodsaver.app.presentation.ProductDetail.ProductDetailActions.OnError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailViewModel(
    private val productId: String,
    private val isProductInCart: Boolean,
    private val getCachedProductUseCase: GetCachedProductUseCase,

    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val increaseProductCountUseCase: IncreaseProductCountUseCase,
    private val decreaseProductCountUseCase: DecreaseProductCountUseCase,
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        ProductDetailState(
            isProductInCart = isProductInCart
        )
    )
    val state = _state.asStateFlow()

    private val _channel = Channel<ProductDetailActions>()
    val channel = _channel.receiveAsFlow()

    init {
        getProduct()
    }

    fun onRefresh() {
        _state.update { it.copy(isRefresh = true) }
        viewModelScope.launch(Dispatchers.InputOutput) {

            val jobs = arrayOf(getProduct())

            joinAll(*jobs)
            delay(750)
            withContext(Dispatchers.Main) {
                _state.update { it.copy(isRefresh = false) }
            }
        }
    }

    private fun getProduct(): Job {
        return viewModelScope.launch(Dispatchers.InputOutput) {
            getCachedProductUseCase(productId).collect { product ->
                _state.update {
                    withContext(Dispatchers.Main) {
                        it.copy(
                            product = product
                        )
                    }
                }
            }
        }
    }

    fun onEvent(events: ProductDetailEvents) {
        when (events) {
            ProductDetailEvents.OnAddProductToCart -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    val request = CartRequestModel(productId, _state.value.productCount)

                    when (val result = addProductToCartUseCase(request)) {
                        is ApiResult.Error -> {
                            _state.value = state.value.copy(isLoading = false)
                            _channel.send(OnError(result.error.message))
                        }

                        ApiResult.Loading -> {
                            _state.value = state.value.copy(isLoading = true)
                        }

                        is ApiResult.Success<CartItemModel> -> {
                            _state.value = state.value.copy(isLoading = false, isProductInCart = true)
                            _channel.send(ProductDetailActions.OnAddedToCart)
                        }
                    }
                }
            }

            ProductDetailEvents.OnDecreaseCountClick -> {
                _state.update {
                    if (it.productCount != 1L) {
                        it.copy(productCount = it.productCount - 1)
                    } else {
                        it
                    }
                }

                if (_state.value.isProductInCart) {
                    viewModelScope.launch(Dispatchers.InputOutput) {
                        val request = CartRequestModel(productId = productId)
                        val result = decreaseProductCountUseCase(request)
                        if (result is ApiResult.Error) {
                            _channel.send(OnError(result.error.message))
                        }
                    }
                }
            }

            ProductDetailEvents.OnIncreaseCountClick -> {
                _state.update { it.copy(productCount = it.productCount + 1) }

                if (_state.value.isProductInCart) {
                    viewModelScope.launch(Dispatchers.InputOutput) {
                        val request = CartRequestModel(productId = productId)
                        val result = increaseProductCountUseCase(request)
                        if (result is ApiResult.Error) {
                            _channel.send(OnError(result.error.message))
                        }
                    }
                }
            }

            ProductDetailEvents.OnRemoveProductFromCart -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    val result = removeProductFromCartUseCase(productId)

                    if (result is ApiResult.Error) {
                        _channel.send(OnError(result.error.message))
                    }
                    if (result is ApiResult.Success) {
                        _state.update { it.copy(isProductInCart = false) }
                    }
                }
            }
        }
    }
}