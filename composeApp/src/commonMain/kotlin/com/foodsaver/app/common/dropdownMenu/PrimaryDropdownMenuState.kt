package com.foodsaver.app.common.dropdownMenu

import androidx.compose.runtime.Composable

data class PrimaryDropdownMenuState(
    val item: @Composable () -> Unit,
    val onItemClick: () -> Unit
)