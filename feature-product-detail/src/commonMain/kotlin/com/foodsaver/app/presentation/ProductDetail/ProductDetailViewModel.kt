package com.foodsaver.app.presentation.ProductDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.InputOutput
import com.foodsaver.app.domain.usecase.GetCachedProductUseCase
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
    private val getCachedProductUseCase: GetCachedProductUseCase
): ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
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
            ProductDetailEvents.AddProductToCart -> TODO()
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
        }
    }
}