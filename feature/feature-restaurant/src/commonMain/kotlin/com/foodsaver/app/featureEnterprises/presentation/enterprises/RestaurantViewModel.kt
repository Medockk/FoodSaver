package com.foodsaver.app.featureEnterprises.presentation.enterprises

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.commonModule.utils.pagination.OfflineFirstPaginator
import com.foodsaver.app.coreCart.domain.model.CartRequestModel
import com.foodsaver.app.coreCart.domain.repository.CartRepository
import com.foodsaver.app.coreEnterprises.domain.repository.RestaurantRepository
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.featureEnterprises.presentation.enterprises.RestaurantAction.OnError
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.getValue

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

    private val _state = MutableStateFlow(RestaurantState(
        restaurantName = navArgs.restaurantName
    ))
    val state = _state.asStateFlow()

    private val pageSize = 10

    private val productPaginator by lazy {
        OfflineFirstPaginator<Int, List<ProductModel>>(
            initialKey = 0,
            onCacheRequest = {
                /* TODO */
                ApiResult.loading()
            },
            onNetworkRequest = { currentKey ->
                productRepository.getProductsByRestaurantId(restaurantId, currentKey, pageSize)
            },
            onSuccess = { _, result ->
                _state.update {
                    it.copy(
                        restaurantProducts = result
                    )
                }
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
    }

    private fun loadProductsByRestaurantId() {
        viewModelScope.launch {
            productPaginator.loadPage()
        }
    }

    private fun loadRestaurantById(restaurantId: String) {
        viewModelScope.launch {
            restaurantRepository.getRestaurantById(restaurantId)
                .onSuccess { restaurant ->
                    _state.update {
                        it.copy(
                            restaurant = restaurant
                        )
                    }
                }.onFailure {
                    sendError(it)
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
                        request = CartRequestModel(
                            productId = event.productId,
                            quantity = 1L
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