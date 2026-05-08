package com.foodsaver.app.featureCart.presentation.cart

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreCart.domain.model.CartItemModel
import com.foodsaver.app.coreCart.domain.model.CartRequestModel
import com.foodsaver.app.coreCart.domain.model.ChangeQuantityRequest
import com.foodsaver.app.coreCart.domain.repository.CartRepository
import com.foodsaver.app.corePaymentMethod.domain.repository.ReadPaymentMethodRepository
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import com.foodsaver.app.coreProfile.domain.usecase.GetProfileUseCase
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.Dispatchers
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
                cartRepository.getCartItems(cartId).collect { result ->
                    result.onSuccess { items ->
                        getProducts(items)
                    }
                }
            }
        }
    }

    private fun getProducts(items: List<CartItemModel>) {
        items.forEach { item ->
            val currentState = _state.value
            if (currentState.products.find { it.productId == item.productId } == null) {
                viewModelScope.launch(Dispatchers.InputOutput) {
                    productRepository.getProductById(item.productId).onSuccess { product ->
                        val item = CartState.CartItem(
                            productName = product.name,
                            productPrice = product.price,
                            productImageUris = product.imageUris,
                            quantityInCart = item.quantity,
                            productSize = "14 ''" /*TODO*/,
                            productId = product.productId,
                            cartItemId = item.cartItemId,
                        )
                        _state.update { it.copy(
                            products = it.products + item
                        ) }
                    }
                }
            }
        }
    }

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.DecreaseProductClick -> {

            }
            is CartEvent.IncreaseProductClick -> {
                val request = ChangeQuantityRequest(
                    cartItemId = event.item.cartItemId,
                    newQuantity = event.item.quantityInCart + 1L
                )
                viewModelScope.launch {
                    cartRepository.changeProductQuantity(request).onSuccess {

                    }
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
            CartEvent.OnPlaceOrderClick -> TODO()
        }
    }

    override fun mapBaseError(message: String): CartAction {
        return CartAction.OnError(message)
    }
}