package com.foodsaver.app.featureFoodDetail.presentation.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.coreProductModule.domain.usecase.GetCachedProductUseCase
import com.foodsaver.app.coreProductModule.domain.usecase.GetProductsUseCase
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.usecase.AddProductToCartUseCase
import com.foodsaver.app.domain.usecase.DecreaseProductCountUseCase
import com.foodsaver.app.domain.usecase.IncreaseProductCountUseCase
import com.foodsaver.app.domain.usecase.RemoveProductFromCartUseCase
import com.foodsaver.app.featureFoodDetail.domain.repository.IngredientsRepository
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

class FoodDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getCachedProductUseCase: GetCachedProductUseCase,
    private val getProductsUseCase: GetProductsUseCase,

    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val increaseProductCountUseCase: IncreaseProductCountUseCase,
    private val decreaseProductCountUseCase: DecreaseProductCountUseCase,
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase,
    private val productRepository: ReadProductRepository,

    private val ingredientsRepository: IngredientsRepository,
) : BaseViewModel<FoodDetailActions>() {

    private val navArgs = savedStateHandle.toRoute<Route.HomeGraph.FoodDetailsScreen>()
    private val _state = MutableStateFlow(
        FoodDetailState(
            isProductInCart = navArgs.isProductInCart,
            productCount = navArgs.initialQuantity
        )
    )
    val state = _state.asStateFlow()

    override val baseChannel: Channel<FoodDetailActions> = Channel()
    override val channel = baseChannel.receiveAsFlow()

    init {
        loadProduct(productId = navArgs.productId)
    }

    private fun loadProduct(productId: String) {
        viewModelScope.launch {
            productRepository.getProductById(productId)
                .onSuccess { product ->
                    _state.update { it.copy(
                        product = product
                    ) }
                }
        }
    }

    fun onRefresh() {

    }

    fun onEvent(events: FoodDetailEvents) {
        when (events) {
            FoodDetailEvents.OnAddProductToCart -> {
                if (_state.value.isProductInCart) return
                viewModelScope.launch(Dispatchers.InputOutput) {
                    val request = CartRequestModel(navArgs.productId, _state.value.productCount)

                    when (val result = addProductToCartUseCase(request)) {
                        is ApiResult.Error -> {
                            _state.value = state.value.copy(isLoading = false)
                            sendError(result)
                        }

                        ApiResult.Loading -> {
                            _state.value = state.value.copy(isLoading = true)
                        }

                        is ApiResult.Success<CartItemModel> -> {
                            _state.value =
                                state.value.copy(isLoading = false, isProductInCart = true)
                            baseChannel.send(FoodDetailActions.OnAddedToCart)
                        }
                        else -> Unit
                    }
                }
            }

            FoodDetailEvents.OnDecreaseCountClick -> {
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
                                sendError(it)
                            }
                    }
                }
            }

            FoodDetailEvents.OnIncreaseCountClick -> {
                _state.update { it.copy(productCount = it.productCount + 1) }

                if (_state.value.isProductInCart) {
                    viewModelScope.launch(Dispatchers.InputOutput) {
                        val request = CartRequestModel(productId = navArgs.productId)
                        increaseProductCountUseCase(request).onFailure {
                            sendError(it)
                        }
                    }
                }
            }

            FoodDetailEvents.OnRemoveProductFromCart -> {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    removeProductFromCartUseCase(navArgs.productId)
                        .onSuccess {
                            _state.update { it.copy(isProductInCart = false) }
                        }.onFailure {
                            sendError(it)
                        }
                }
            }

            FoodDetailEvents.OnAnalyzeIngredients -> {
                viewModelScope.launch {
                    _state.update { it.copy(isAiResponseLoading = true) }
                    ingredientsRepository.analyzeIngredientsByProductId(navArgs.productId)
                        .collectRequest(
                            onSuccess = { data ->
                                if (data == null) {
                                    _state.update {
                                        it.copy(
                                            isAiResponseLoading = false,
                                            ingredientsAIDescription = "I can't say anything about this ingredients"
                                        )
                                    }
                                } else {
                                    _state.update {
                                        it.copy(
                                            ingredientsAIDescription =
                                                ((it.ingredientsAIDescription ?: "") + data)
                                                    .replace("*", ""),
                                            isAiResponseLoading = false
                                        )
                                    }
                                }
                            },
                            onError = { error ->
                                _state.update {
                                    it.copy(
                                        isAiResponseLoading = false
                                    )
                                }
                                sendError(error)
                            }
                        )
                }
            }

            FoodDetailEvents.OnCloseIngredientMenu -> {
                _state.update {
                    it.copy(
                        isIngredientMenuExpanded = false
                    )
                }
            }

            FoodDetailEvents.OnOpenIngredientMenu -> {
                _state.update {
                    it.copy(
                        isIngredientMenuExpanded = true
                    )
                }
            }
        }
    }

    override fun mapBaseError(message: String): FoodDetailActions {
        return FoodDetailActions.OnError(message)
    }
}