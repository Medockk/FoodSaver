package com.foodsaver.app.featureFoodDetail.presentation.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreCart.domain.model.AddProductToCartRequestModel
import com.foodsaver.app.coreCart.domain.model.CartItemAttributes
import com.foodsaver.app.coreCart.domain.model.CartItemModel
import com.foodsaver.app.coreCart.domain.model.DeleteCartItemRequestModel
import com.foodsaver.app.coreCart.domain.repository.CartRepository
import com.foodsaver.app.coreCart.domain.usecase.AddProductToCartUseCase
import com.foodsaver.app.coreCart.domain.usecase.RemoveProductFromCartUseCase
import com.foodsaver.app.coreIngredients.domain.repository.IngredientRepository
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
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

    private val ingredientsRepository: IngredientRepository,
) : BaseViewModel<FoodDetailActions>() {

    private val navArgs = savedStateHandle.toRoute<Route.MainGraph.FoodDetailsScreen>()
    private val _state = MutableStateFlow(
        FoodDetailState(
            productName = navArgs.productName,
            isProductInCart = navArgs.productCartItemId != null,
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
            productRepository.observeProductById(productId)
                .collectRequest(
                    onSuccess = { product ->
                        println("Get info about product ${product.productId}\n" +
                                "ImageUris for this product: ${product.imageUris}")
                        _state.update {
                            it.copy(
                                product = product
                            )
                        }

                        loadIngredients(product)
                    })
        }
    }

    private fun loadIngredients(productModel: ProductModel) {
        viewModelScope.launch {
            println("Ingredients ${productModel.ingredientIds}")
            ingredientsRepository.fetchIngredientsByIds(productModel.ingredientIds)
                .onSuccess { ingredients ->
                    _state.update { it.copy(
                        ingredients = ingredients
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
                    val request = AddProductToCartRequestModel(
                        productId = navArgs.productId,
                        quantity = _state.value.productCount,
                        attributes = CartItemAttributes() // TODO
                    )

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
                            navArgs.productCartItemId = result.data.serverId
                            baseChannel.send(FoodDetailActions.OnAddedToCart)
                        }

                        else -> Unit
                    }
                }
            }

            FoodDetailEvents.OnDecreaseCountClick -> {
                val newCount = if (_state.value.productCount < 1L) 1L
                else _state.value.productCount - 1L
                _state.update {
                    it.copy(
                        productCount = newCount
                    )
                }
            }

            FoodDetailEvents.OnIncreaseCountClick -> {
                val newCount = _state.value.productCount + 1L
                _state.update {
                    it.copy(productCount = newCount)
                }
            }

            FoodDetailEvents.OnRemoveProductFromCart -> {
                if (!_state.value.isProductInCart) return
                viewModelScope.launch(Dispatchers.InputOutput) {
                    navArgs.productCartItemId?.let { cartItemId ->
                        val request = DeleteCartItemRequestModel(
                            localId = "",
                            cartItemId = cartItemId
                        )
                        cartRepository.removeProductFromCart(request).onSuccess {
                            _state.update { it.copy(
                                isProductInCart = false
                            ) }
                        }
                    }
                }
            }

            FoodDetailEvents.OnAnalyzeIngredients -> {
                viewModelScope.launch {
                    _state.update { it.copy(isAiResponseLoading = true) }
                    TODO()
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