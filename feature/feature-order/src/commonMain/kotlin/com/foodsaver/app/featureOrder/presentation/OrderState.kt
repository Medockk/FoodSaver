package com.foodsaver.app.featureOrder.presentation

import com.foodsaver.app.featureOrder.domain.model.OrderModel

data class OrderState(
    val tabIndex: Int = 0,

    val ongoingOrders: List<OrderModel> = emptyList(),
    val historyOrders: List<OrderModel> = emptyList(),

    val isQrCodeDialogVisible: Boolean = false,
)
