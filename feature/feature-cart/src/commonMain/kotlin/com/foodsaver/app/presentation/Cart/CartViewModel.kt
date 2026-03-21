package com.foodsaver.app.presentation.Cart

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.corePaymentMethod.domain.repository.ReadPaymentMethodRepository
import com.foodsaver.app.coreProfile.domain.usecase.GetProfileUseCase
import com.foodsaver.app.domain.usecase.GetCartUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val getProfileUseCase: GetProfileUseCase,

    private val readPaymentMethodRepository: ReadPaymentMethodRepository
) : BaseViewModel<CartAction>() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    override val baseChannel: Channel<CartAction> = Channel()
    override val channel = baseChannel.receiveAsFlow()

    init {
        getCart()
        getProfile()
        getPaymentMethod()
    }

    private fun getPaymentMethod() = viewModelScope.launch(Dispatchers.InputOutput) {
        readPaymentMethodRepository.getCurrentPaymentMethod().collectRequest(
            onSuccess = { paymentMethod ->
                _state.update { it.copy(paymentMethod = paymentMethod) }
            }
        )
    }

    private fun getProfile() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase().collectRequest(
                onSuccess = {
                    _state.update { state ->
                        state.copy(
                            profile = it
                        )
                    }
                }
            )
        }
    }

    private fun getCart() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getCartUseCase.invoke().collectRequest(
                onSuccess = { response ->
                    _state.update {
                        it.copy(
                            cartProducts = response,
                            isLoading = false
                        )
                    }
                },
                onError = { error ->
                    _state.update { it.copy(isLoading = false) }
                    sendError(error.message)
                },
                onLoading = {
                    _state.update { it.copy(isLoading = true) }
                }
            )
        }
    }

    fun onEvent(event: CartEvent) {
        when (event) {
            CartEvent.OnClearCart -> TODO()
            is CartEvent.OnDecreaseProductCount -> TODO()
            is CartEvent.OnDeleteProduct -> TODO()
            is CartEvent.OnIncreaseProductCount -> TODO()
        }
    }

    override fun mapBaseError(message: String): CartAction {
        return CartAction.OnError(message)
    }
}