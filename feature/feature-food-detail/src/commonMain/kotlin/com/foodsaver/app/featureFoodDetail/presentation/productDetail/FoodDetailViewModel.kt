package com.foodsaver.app.featureFoodDetail.presentation.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreCart.domain.model.CartItemModel
import com.foodsaver.app.coreCart.domain.model.CartRequestModel
import com.foodsaver.app.coreCart.domain.model.ChangeQuantityRequest
import com.foodsaver.app.coreCart.domain.repository.CartRepository
import com.foodsaver.app.coreCart.domain.usecase.AddProductToCartUseCase
import com.foodsaver.app.coreCart.domain.usecase.RemoveProductFromCartUseCase
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.featureFoodDetail.domain.repository.IngredientsRepository
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FoodDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val cartRepository: CartRepository,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase,
    private val productRepository: ReadProductRepository,

    private val ingredientsRepository: IngredientsRepository,
) : BaseViewModel<FoodDetailActions>() {

    private val navArgs = savedStateHandle.toRoute<Route.HomeGraph.FoodDetailsScreen>()
    private val _state = MutableStateFlow(
        FoodDetailState(
            productName = navArgs.productName,
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

                    product.imageUris.firstOrNull()?.let { uri ->
                        loadProductColor(uri)
                    }
                }
        }
    }

    private fun loadProductColor(imageUri: String) {
        viewModelScope.launch(Dispatchers.InputOutput) {

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
                TODO()
            }

            FoodDetailEvents.OnIncreaseCountClick -> {
                TODO()
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

            is FoodDetailEvents.OnChangeSelectedImageIndex -> {
                _state.update {
                    it.copy(
                        selectedImageIndex = events.index
                    )
                }
            }
        }
    }

    override fun mapBaseError(message: String): FoodDetailActions {
        return FoodDetailActions.OnError(message)
    }
}