package com.foodsaver.app.featureMyFood.presentation

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyFoodViewModel(
    private val productRepository: ReadProductRepository
): BaseViewModel<MyFoodAction>() {

    private val _state = MutableStateFlow(MyFoodState())
    val state = _state.asStateFlow()

    init {
        observeUserProducts()
        fetchProducts()
    }

    private fun observeUserProducts() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            productRepository.observeUserProducts().collect { result ->
                result.onSuccess { products ->
                    _state.update { it.copy(
                        products = products
                    ) }
                }
            }
        }
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            productRepository.fetchUserProducts()
        }
    }

    override fun mapBaseError(message: String): MyFoodAction {
        return MyFoodAction.OnError(message)
    }
}