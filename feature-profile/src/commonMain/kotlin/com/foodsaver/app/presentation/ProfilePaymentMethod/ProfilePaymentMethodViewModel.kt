package com.foodsaver.app.presentation.ProfilePaymentMethod

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.InputOutput
import com.foodsaver.app.domain.usecase.paymentCard.GetPaymentMethodUseCase
import com.foodsaver.app.model.PaymentCardModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfilePaymentMethodViewModel(
    private val getPaymentMethodUseCase: GetPaymentMethodUseCase
): ViewModel() {

    var state by mutableStateOf(ProfilePaymentMethodState())
        private set

    private val _channel = Channel<ProfilePaymentMethodAction>()
    val channel = _channel.receiveAsFlow()

    init {
        getPaymentMethods()
    }

    private fun getPaymentMethods() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getPaymentMethodUseCase().collect {
                when (it) {
                    is ApiResult.Error -> {
                        state = state.copy(isLoading = true)
                        _channel.send(ProfilePaymentMethodAction.OnError(it.error.message))
                    }
                    ApiResult.Loading -> {
                        state = state.copy(isLoading = true)
                    }
                    is ApiResult.Success<List<PaymentCardModel>> -> {
                        withContext(Dispatchers.Main) {
                            println("Cards ${it.data}")
                            state = state.copy(
                                cards = it.data
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: ProfilePaymentMethodEvent) {
        when (event) {
            ProfilePaymentMethodEvent.OnAddNewCardClick -> TODO()
        }
    }
}