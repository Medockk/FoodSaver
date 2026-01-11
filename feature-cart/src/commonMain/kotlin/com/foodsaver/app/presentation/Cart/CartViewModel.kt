package com.foodsaver.app.presentation.Cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.usecase.GetCartUseCase
import com.foodsaver.app.domain.usecase.GetProfileUseCase
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
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val _channel = Channel<CartAction>()
    val channel = _channel.receiveAsFlow()

    init {
        getCart()
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getProfileUseCase().collect {
                when (it) {
                    is ApiResult.Error -> {
                        _channel.send(CartAction.OnError(it.error.message))
                    }

                    ApiResult.Loading -> Unit
                    is ApiResult.Success<UserModel> -> {
                        _state.update { state ->
                            state.copy(
                                profile = it.data
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getCart() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getCartUseCase.invoke().collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        _state.update { it.copy(isLoading = false) }

                        _channel.send(CartAction.OnError(result.error.message))
                    }

                    ApiResult.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }

                    is ApiResult.Success<List<CartItemModel>> -> {
                        println("Cart Screen. CHANGES: ${result.data.map { it.globalId + " " + it.quantity }}")
                        _state.update {
                            it.copy(
                                cartProducts = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
            }
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
}