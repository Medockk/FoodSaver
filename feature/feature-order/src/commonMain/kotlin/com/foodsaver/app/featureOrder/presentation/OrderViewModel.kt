package com.foodsaver.app.featureOrder.presentation

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreModel.model.order.OrderStatus
import com.foodsaver.app.featureOrder.domain.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository
) : BaseViewModel<OrderAction>() {

    private val _state = MutableStateFlow(OrderState())
    val state = _state.asStateFlow()

    init {
        observeOrders()
    }

    private fun observeOrders() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            orderRepository.observeOrders().collectRequest(
                onSuccess = { orders ->
                    val historyOrders = orders.filter {
                        it.status == OrderStatus.DELIVERED || it.status == OrderStatus.CANCELLED
                    }
                    val ongoingOrders = orders.filter {
                        it.status != OrderStatus.DELIVERED || it.status != OrderStatus.CANCELLED
                    }
                    _state.update {
                        it.copy(
                            historyOrders = historyOrders,
                            ongoingOrders = ongoingOrders
                        )
                    }
                }
            )
        }
    }

    fun onEvent(event: OrderEvent) {
        when (event) {
            is OrderEvent.OnTabIndexChange -> {
                _state.update { it.copy(tabIndex = event.index) }
            }
        }
    }

    override fun mapBaseError(message: String): OrderAction {
        return OrderAction.OnError(message)
    }
}