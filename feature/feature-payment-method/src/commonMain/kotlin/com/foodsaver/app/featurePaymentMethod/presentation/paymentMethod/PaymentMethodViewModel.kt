package com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.corePaymentMethod.domain.repository.EditPaymentMethodRepository
import com.foodsaver.app.corePaymentMethod.domain.repository.ReadPaymentMethodRepository
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentMethodViewModel(
    savedStateHandle: SavedStateHandle,
    private val readPaymentMethodRepository: ReadPaymentMethodRepository,
    private val editPaymentMethodRepository: EditPaymentMethodRepository
): BaseViewModel<PaymentMethodAction>() {

    private val navArgs = savedStateHandle.toRoute<Route.PaymentMethodGraph.PaymentMethodScreen>()
    private val _state = MutableStateFlow(PaymentMethodState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(
            totalPrice = navArgs.price,
            currency = navArgs.currency
        ) }

        observeCurrentPaymentMethod()
    }

    private fun observeCurrentPaymentMethod() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            readPaymentMethodRepository.observeCurrentPaymentMethod()
                .collect { result ->
                    result.onSuccess { method ->
                        _state.update { it.copy(
                            currentPaymentMethodCardModel = method,
                            currentPaymentMethodType = method?.type
                        ) }
                    }
                }
        }
    }

    fun onEvent(event: PaymentMethodEvent) {
        when (event) {
            is PaymentMethodEvent.OnChangePaymentMethod -> TODO()
            PaymentMethodEvent.OnPayClick -> TODO()
        }
    }

    override fun mapBaseError(message: String): PaymentMethodAction {
        return PaymentMethodAction.OnError(message)
    }
}