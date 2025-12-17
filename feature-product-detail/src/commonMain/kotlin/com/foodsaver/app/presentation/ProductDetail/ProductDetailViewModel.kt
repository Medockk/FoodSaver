package com.foodsaver.app.presentation.ProductDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.InputOutput
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.usecase.AddProductToCartUseCase
import com.foodsaver.app.domain.usecase.GetCachedProductUseCase
import com.foodsaver.app.model.ProductModel
import com.foodsaver.app.presentation.ProductDetail.ProductDetailActions.OnError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailViewModel(
    private val productId: String,
    private val isProductInCart: Boolean,
    private val getCachedProductUseCase: GetCachedProductUseCase,

    private val addProductToCartUseCase: AddProductToCartUseCase,
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

    private fun getProduct() {
        viewModelScope.launch(Dispatchers.InputOutput) {
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
                            _state.value = state.value.copy(isLoading = false)
                            _channel.send(ProductDetailActions.OnAddedToCart)
                        }
                    }
                }
            }

            ProductDetailEvents.OnDecreaseCountClick -> {
                _state.update {
                    if (it.productCount != 1) {
                        it.copy(productCount = it.productCount - 1)
                    } else {
                        it
                    }
                }
            }

            ProductDetailEvents.OnIncreaseCountClick -> {
                _state.update { it.copy(productCount = it.productCount + 1) }
            }

            ProductDetailEvents.OnRemoveProductFromCart -> {
                viewModelScope.launch(Dispatchers.InputOutput) {

                }
            }
        }
    }
}