package com.foodsaver.app.presentation.FeatureMain.Product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsaver.app.InputOutput
import com.foodsaver.app.domain.model.ProductModel
import com.foodsaver.app.domain.usecase.GetCachedProductUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PVM(
    private val getCachedProductUseCase: GetCachedProductUseCase
): ViewModel() {

    private val _state = MutableStateFlow<ProductModel?>(null)
    val state = _state.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch(Dispatchers.InputOutput) {
            getCachedProductUseCase.invoke(productId).collect { product ->
                _state.value = product
            }
        }
    }
}