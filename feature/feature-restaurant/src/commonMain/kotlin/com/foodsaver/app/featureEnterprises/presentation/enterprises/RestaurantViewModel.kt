package com.foodsaver.app.featureEnterprises.presentation.enterprises

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.commonModule.utils.pagination.BasePaginator
import com.foodsaver.app.coreCart.domain.model.AddProductToCartRequestModel
import com.foodsaver.app.coreCart.domain.model.CartItemAttributes
import com.foodsaver.app.coreCart.domain.repository.CartRepository
import com.foodsaver.app.coreEnterprises.domain.repository.RestaurantRepository
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.featureEnterprises.presentation.enterprises.RestaurantAction.OnError
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RestaurantViewModel(
    private val restaurantRepository: RestaurantRepository,
    private val productRepository: ReadProductRepository,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<RestaurantAction>() {

    override val baseChannel: Channel<RestaurantAction> = Channel()
    override val channel: Flow<RestaurantAction> = baseChannel.receiveAsFlow()

    private val navArgs = savedStateHandle.toRoute<Route.HomeGraph.Restaurant>()
    private val restaurantId = navArgs.restaurantId

    private val _state = MutableStateFlow(
        RestaurantState(
            restaurantName = navArgs.restaurantName
        )
    )
    val state = _state.asStateFlow()

    private val pageSize = 10

    private val productPaginator by lazy {
        BasePaginator<Int, List<ProductModel>>(
            initialKey = 0,
            onRequest = { currentKey ->
                productRepository.fetchProductByRestaurantId(restaurantId, currentKey, pageSize)
            },
            onSuccess = { _, result ->
                // collecting data from observe function
            },
            onError = { sendError(it) },
            onNextKey = { currentKey, result -> currentKey + 1 },
            onLoadUpdated = { isLoading ->
                _state.update {
                    it.copy(
                        isProductsLoading = isLoading
                    )
                }
            },
            onEndReaching = { _, result -> result.size < pageSize }
        )
    }

    init {
        // find restaurant by id
        loadRestaurantById(restaurantId)
        loadProductsByRestaurantId()
        loadProductsInCart()
    }

    private fun loadProductsInCart() {
        viewModelScope.launch {
            cartRepository.observeCartProductIds().collectRequest(
                onSuccess = { ids ->
                    _state.update { it.copy(
                        productInCartIds = ids.toSet()
                    ) }
                }
            )
        }
    }

    private fun loadProductsByRestaurantId() {
        viewModelScope.launch {
            productPaginator.loadPage()
        }
    }

    private fun loadRestaurantById(restaurantId: String) {
        viewModelScope.launch(Dispatchers.InputOutput) {
            productRepository.observeProductsByRestaurantId(restaurantId).collect { apiResult ->
                apiResult.onSuccess { products ->
                    _state.update {
                        it.copy(
                            restaurantProducts = products,

                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: RestaurantEvent) {
        when (event) {
            is RestaurantEvent.OnSelectedImageIndexChange -> {
                _state.update {
                    it.copy(
                        selectedImageIndex = event.index
                    )
                }
            }

            is RestaurantEvent.OnAddProductToCart -> {
                viewModelScope.launch {
                    cartRepository.addProductToCart(
                        request = AddProductToCartRequestModel(
                            productId = event.productId,
                            quantity = 1L,
                            attributes = CartItemAttributes() // TODO
                        )
                    )
                }
            }
        }
    }

    override fun mapBaseError(message: String): RestaurantAction {
        return OnError(message)
    }
}