package com.foodsaver.app.featureProductDetail.presentation.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.onFailure
import com.foodsaver.app.commonModule.ApiResult.onSuccess
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.usecase.AddProductToCartUseCase
import com.foodsaver.app.domain.usecase.DecreaseProductCountUseCase
import com.foodsaver.app.domain.usecase.GetCachedProductUseCase
import com.foodsaver.app.domain.usecase.IncreaseProductCountUseCase
import com.foodsaver.app.domain.usecase.RemoveProductFromCartUseCase
import com.foodsaver.app.navigationModule.Route
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
    savedStateHandle: SavedStateHandle,
    private val getCachedProductUseCase: GetCachedProductUseCase,

    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val increaseProductCountUseCase: IncreaseProductCountUseCase,
    private val decreaseProductCountUseCase: DecreaseProductCountUseCase,
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase,
) : BaseViewModel<ProductDetailActions>() {

    private val navArgs = savedStateHandle.toRoute<Route.MainGraph.ProductDetailScreen>()
    private val _state = MutableStateFlow(
        ProductDetailState(
            isProductInCart = navArgs.isProductInCart,
            productCount = navArgs.initialQuantity
        )
    )
    val state = _state.asStateFlow()

    override val baseChannel: Channel<ProductDetailActions> = Channel()
    val channel = baseChannel.receiveAsFlow()

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
            getCachedProductUseCase.invoke(navArgs.productId).collect { product ->
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
                if (_state.value.isProductInCart) return
                viewModelScope.launch(Dispatchers.InputOutput) {
                    val request = CartRequestModel(navArgs.productId, _state.value.productCount)

                    when (val result = addProductToCartUseCase(request)) {
                        is ApiResult.Error -> {
                            _state.value = state.value.copy(isLoading = false)
                            sendError(result.error.message)
                        }

                        ApiResult.Loading -> {
                            _state.value = state.value.copy(isLoading = true)
                        }

                        is ApiResult.Success<CartItemModel> -> {
                            _state.value =
                                state.value.copy(isLoading = false, isProductInCart = true)
                            baseChannel.send(ProductDetailActions.OnAddedToCart)
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
                        val request = CartRequestModel(productId = navArgs.productId)
                        decreaseProductCountUseCase(request)
                            .onFailure {
                                sendError(it.message)
                            }
                    }
                }
            }

            ProductDetailEvents.OnIncreaseCountClick -> {
                _state.update { it.copy(productCount = it.productCount + 1) }

                if (_state.value.isProductInCart) {
                    viewModelScope.launch(Dispatchers.InputOutput) {
                        val request = CartRequestModel(productId = navArgs.productId)
                        increaseProductCountUseCase(request).onFailure {
                            sendError(it.message)
                        }
                    }
                }
            }

            ProductDetailEvents.OnRemoveProductFromCart -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    removeProductFromCartUseCase(navArgs.productId)
                        .onSuccess {
                            _state.update { it.copy(isProductInCart = false) }
                        }.onFailure {
                            sendError(it.message)
                        }
                }
            }
        }
    }

    override fun mapBaseError(message: String): ProductDetailActions {
        return ProductDetailActions.OnError(message)
    }
}