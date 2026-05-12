package com.foodsaver.app.common.searchField

import androidx.compose.ui.text.input.TextFieldValue

data class SearchTextFieldState(
    val query: TextFieldValue,
    val onQueryChange: (TextFieldValue) -> Unit,
    val onSearch: (TextFieldValue) -> Unit,
    val suggestion: String? = null,
    val enabled: Boolean = true,
)
