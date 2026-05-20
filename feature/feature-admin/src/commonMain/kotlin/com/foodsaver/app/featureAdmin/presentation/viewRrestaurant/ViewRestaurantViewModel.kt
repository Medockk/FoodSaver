package com.foodsaver.app.featureAdmin.presentation.viewRrestaurant

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreRestaurant.domain.repository.EditRestaurantRepository
import com.foodsaver.app.coreRestaurant.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewRestaurantViewModel(
    private val restaurantRepository: RestaurantRepository,
    private val editRestaurantRepository: EditRestaurantRepository
): BaseViewModel<ViewRestaurantAction>() {

    private val _state = MutableStateFlow(ViewRestaurantState())
    val state = _state.asStateFlow()

    init {
        observeRestaurants()
    }

    private fun observeRestaurants() {
        viewModelScope.launch {
            restaurantRepository.observeRestaurants().collect { result ->
                result.onSuccess { restaurants ->
                    _state.update { it.copy(
                        restaurants = restaurants
                    ) }
                }
            }
        }
    }

    override fun mapBaseError(message: String): ViewRestaurantAction {
        return ViewRestaurantAction.OnError(message)
    }
}