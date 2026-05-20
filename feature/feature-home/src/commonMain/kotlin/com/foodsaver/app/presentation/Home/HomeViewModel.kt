@file:OptIn(FlowPreview::class)

package com.foodsaver.app.presentation.Home

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.commonModule.utils.pagination.OfflineFirstPaginator
import com.foodsaver.app.coreCart.domain.repository.CartRepository
import com.foodsaver.app.coreCategory.domain.repository.CategoryRepository
import com.foodsaver.app.coreRestaurant.domain.repository.RestaurantRepository
import com.foodsaver.app.coreProfile.domain.repository.ProfileRepository
import com.foodsaver.app.presentation.Home.HomeAction.OnError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val restaurantRepository: RestaurantRepository,

    private val cartRepository: CartRepository,
    private val profileRepository: ProfileRepository,
) : BaseViewModel<HomeAction>() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    override val baseChannel: Channel<HomeAction> = Channel()
    override val channel = baseChannel.receiveAsFlow()

    private val restaurantPaginator = OfflineFirstPaginator(
        initialKey = 0,
        onCacheRequest = {
            restaurantRepository.getCachedRestaurants()
        },
        onNetworkRequest = { page ->
            restaurantRepository.getAllRestaurants(page, 10)
        },
        onSuccess = { _, result ->
            _state.update {
                it.copy(
                    restaurants = result
                )
            }
        },
        onError = { error ->
            sendError(error)
        },
        onNextKey = { currentKey, _ ->
            currentKey + 1
        },
        onLoadUpdated = { isLoading ->
            _state.update {
                it.copy(
                    isRestaurantsLoading = isLoading
                )
            }
        },
        onEndReaching = { _, result ->
            result.size < 10
        }
    )

    init {
        loadRestaurants()
        observeCart()
        getAllCategories()
        getCurrentAddress()

        observeProfile()
    }

    private fun observeProfile() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            profileRepository.observeProfile().collectRequest(
                onSuccess = { profile ->
                    _state.update { it.copy(
                        profile = profile
                    ) }
                }
            )
        }
    }

    private fun loadRestaurants() {
        viewModelScope.launch(Dispatchers.InputOutput) {
            restaurantPaginator.loadPage()
        }
    }

    private fun fetchCart() {
        viewModelScope.launch {
            cartRepository.fetchCart()
        }
    }

    private fun getCurrentAddress() = viewModelScope.launch(Dispatchers.InputOutput) {
        // TODO
    }

    fun onRefresh(): Job = viewModelScope.launch {
        loadRestaurants()
        getAllCategories()
        getCurrentAddress()

        fetchCart()
    }

    private fun getAllCategories(): Job {
        return viewModelScope.launch(Dispatchers.InputOutput) {
            _state.update { it.copy(isCategoriesLoading = true) }
            categoryRepository.fetchAllCategories().onSuccess { result ->
                _state.update { it.copy(categories = result, isCategoriesLoading = false) }
            }
        }
    }

    private fun observeCart(): Job = viewModelScope.launch(Dispatchers.InputOutput) {
        cartRepository.observeCart().collectRequest(
            onSuccess = { result ->
                _state.update {
                    it.copy(
                        cartSize = result.quantity,
                        cartId = result.cartId,
                        cartPrice = result.finalPrice
                    )
                }
            }
        )
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnCategoryIndexChange -> {
                TODO("navigate to search screen!")
            }

            is HomeEvent.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = event.value) }
                // TODO navigate to search screen!
            }

            HomeEvent.LoadNextRestaurants -> {
                loadRestaurants()
            }

            HomeEvent.OnRefresh -> {
                _state.update { it.copy(isRefresh = true) }
                onRefresh().invokeOnCompletion {
                    _state.update { it.copy(isRefresh = false) }
                }
            }
        }
    }

    override fun mapBaseError(message: String): HomeAction {
        return OnError(message)
    }
}