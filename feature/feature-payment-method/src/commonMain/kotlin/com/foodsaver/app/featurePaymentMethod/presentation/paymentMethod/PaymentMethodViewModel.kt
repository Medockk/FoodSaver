package com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.corePaymentMethod.domain.repository.EditPaymentMethodRepository
import com.foodsaver.app.corePaymentMethod.domain.repository.ReadPaymentMethodRepository
import com.foodsaver.app.featurePaymentMethod.domain.OrderRepository
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentMethodViewModel(
    savedStateHandle: SavedStateHandle,
    private val readPaymentMethodRepository: ReadPaymentMethodRepository,
    private val editPaymentMethodRepository: EditPaymentMethodRepository,

    private val orderRepository: OrderRepository
): BaseViewModel<PaymentMethodAction>() {

    private val navArgs = savedStateHandle.toRoute<Route.PaymentMethodGraph.PaymentMethodScreen>()
    private val _state = MutableStateFlow(PaymentMethodState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            totalPrice = navArgs.price,
            currency = navArgs.currency
        ) }

        observePaymentMethodTypes()
        observePaymentMethods()
    }

    private fun observePaymentMethodTypes() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            readPaymentMethodRepository.observePaymentMethodTypes()
                .collect { result ->
                    result.onSuccess { types ->
                        _state.update { it.copy(
                            paymentMethodTypes = types
                        ) }
                    }
                }
        }
    }

    private fun observePaymentMethods() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            readPaymentMethodRepository.observePaymentMethods()
                .collect { result ->
                    result.onSuccess { methods ->
                        _state.update { it.copy(
                            paymentMethods = methods
                        ) }
                    }
                }
        }
    }

    fun onEvent(event: PaymentMethodEvent) {
        when (event) {
            is PaymentMethodEvent.OnChangePaymentMethod -> {
                val methodsByType = _state.value.paymentMethods
                    .filter { it.type.id == event.type.id }
                _state.update { it.copy(
                    currentPaymentMethodType = event.type,
                    selectedPaymentTypeIndex = event.index,
                    paymentMethodsByType = methodsByType
                ) }
            }
            PaymentMethodEvent.OnPayClick -> {
                viewModelScope.launch {
                    orderRepository.makeOrder().onSuccess {
                        baseChannel.send(PaymentMethodAction.OnSuccessfulPay)
                    }.onFailure {
                        sendError(it)
                    }

                }
            }
        }
    }

    override fun mapBaseError(message: String): PaymentMethodAction {
        return PaymentMethodAction.OnError(message)
    }
}