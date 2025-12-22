package com.foodsaver.app.presentation.Cart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.InputOutput
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.usecase.GetCartUseCase
import com.foodsaver.app.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val getProfileUseCase: GetProfileUseCase
): ViewModel() {

    private val _state = mutableStateOf(CartState())
    val state: State<CartState> = _state

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
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(
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
            getCartUseCase.invoke().collect {
                when (it) {
                    is ApiResult.Error -> {
                        _state.value = state.value.copy(isLoading = false)

                        _channel.send(CartAction.OnError(it.error.message))
                    }
                    ApiResult.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }
                    is ApiResult.Success<List<CartItemModel>> -> {
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(
                                cartProducts = it.data,
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