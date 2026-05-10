package com.foodsaver.app.featureCart.presentation.cart

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreCart.domain.model.ChangeQuantityRequest
import com.foodsaver.app.coreCart.domain.model.DeleteCartItemRequestModel
import com.foodsaver.app.coreCart.domain.repository.CartRepository
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    savedStateHandle: SavedStateHandle,
    private val cartRepository: CartRepository,
    private val productRepository: ReadProductRepository
) : BaseViewModel<CartAction>() {

    private val navArgs = savedStateHandle.toRoute<Route.CartGraph.CartScreen>()

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    override val baseChannel: Channel<CartAction> = Channel()
    override val channel = baseChannel.receiveAsFlow()

    init {
        getCartItems()
    }

    private fun getCartItems() {
        navArgs.cartId?.let { cartId ->
            viewModelScope.launch {
                cartRepository.observeCartItems(cartId).collect { result ->
                    result.onSuccess { items ->
                        _state.update { it.copy(
                            products = items
                        ) }
                    }
                }
            }
        }
    }

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.DecreaseProductClick -> {
                val newQuantity = if (event.item.quantity < 1L) 1L
                else event.item.quantity - 1L
                val request = ChangeQuantityRequest(
                    cartItemId = event.item.serverId,
                    newQuantity = newQuantity,
                    localId = event.item.localId
                )
                viewModelScope.launch {
                    cartRepository.changeProductQuantity(request)
                }
            }
            is CartEvent.IncreaseProductClick -> {
                val request = ChangeQuantityRequest(
                    cartItemId = event.item.serverId,
                    newQuantity = event.item.quantity + 1L,
                    localId = event.item.localId
                )
                viewModelScope.launch {
                    cartRepository.changeProductQuantity(request)
                }
            }
            is CartEvent.OnAddressValueChange -> {
                _state.update {
                    it.copy(
                        deliveryAddress = event.value
                    )
                }
            }
            CartEvent.OnEditDeliveryAddressClick -> {
                _state.update {
                    it.copy(
                        isDeliveryAddressEditing = !it.isDeliveryAddressEditing
                    )
                }
            }
            CartEvent.OnEditItemsClick -> {
                _state.update {
                    it.copy(
                        isItemsEditing = !it.isItemsEditing
                    )
                }
            }

            CartEvent.OnPlaceOrderClick -> {

            }
            is CartEvent.OnDeleteItem -> {
                viewModelScope.launch {
                    val request = DeleteCartItemRequestModel(
                        localId = event.item.localId,
                        cartItemId = event.item.serverId
                    )
                    cartRepository.removeProductFromCart(request)
                }
            }
        }
    }

    override fun mapBaseError(message: String): CartAction {
        return CartAction.OnError(message)
    }
}