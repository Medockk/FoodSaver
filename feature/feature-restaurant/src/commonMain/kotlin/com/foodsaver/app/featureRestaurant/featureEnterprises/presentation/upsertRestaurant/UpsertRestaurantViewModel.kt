package com.foodsaver.app.featureRestaurant.featureEnterprises.presentation.upsertRestaurant

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreRestaurant.domain.model.AddRestaurantRequest
import com.foodsaver.app.coreRestaurant.domain.model.UpdateRestaurantRequest
import com.foodsaver.app.coreRestaurant.domain.repository.EditRestaurantRepository
import com.foodsaver.app.coreRestaurant.domain.repository.RestaurantRepository
import com.foodsaver.app.navigationModule.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpsertRestaurantViewModel(
    savedStateHandle: SavedStateHandle,
    private val restaurantRepository: RestaurantRepository,
    private val editRestaurantRepository: EditRestaurantRepository
) : BaseViewModel<UpsertRestaurantAction>() {

    private val _state = MutableStateFlow(UpsertRestaurantState())
    val state = _state.asStateFlow()

    private val navArgs = savedStateHandle.toRoute<Route.AdminGraph.UpsertRestaurantScreen>()

    init {
        navArgs.restaurantId?.let { restaurantId ->
            fetchRestaurantById(restaurantId)
        }
    }

    private fun fetchRestaurantById(restaurantId: String) {
        viewModelScope.launch {
            restaurantRepository.getRestaurantById(restaurantId).onSuccess { restaurant ->
                _state.update {
                    it.copy(
                        restaurantModel = restaurant
                    )
                }
            }
        }
    }

    fun onEvent(event: UpsertRestaurantEvent) {
        when (event) {
            is UpsertRestaurantEvent.OnAddressNameChange -> {
                _state.update {
                    it.copy(
                        restaurantModel = it.restaurantModel?.copy(
                            addressName = event.value
                        ),
                        addressName = event.value
                    )
                }
            }

            is UpsertRestaurantEvent.OnAverageDeliveryTimeChange -> {
                _state.update {
                    it.copy(
                        restaurantModel = it.restaurantModel?.copy(
                            averageDeliveryTime = event.value.toDoubleOrNull()
                        ),
                        averageDeliveryTime = event.value.toDoubleOrNull()
                    )
                }
            }

            is UpsertRestaurantEvent.OnChangeGalleryPickerVisibility -> {
                _state.update {
                    it.copy(
                        isGalleryPickerVisible = event.value
                    )
                }
            }

            is UpsertRestaurantEvent.OnDeliveryCostChange -> {
                _state.update {
                    it.copy(
                        restaurantModel = it.restaurantModel?.copy(
                            deliveryCost = event.value.toDoubleOrNull()
                        ),
                        deliveryCost = event.value.toDoubleOrNull()
                    )
                }
            }

            is UpsertRestaurantEvent.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        restaurantModel = it.restaurantModel?.copy(
                            description = event.value
                        ),
                        description = event.value
                    )
                }
            }

            is UpsertRestaurantEvent.OnLatitudeChange -> {
                _state.update {
                    it.copy(
                        restaurantModel = it.restaurantModel?.copy(
                            latitude = event.value.toDoubleOrNull() ?: 0.0
                        ),
                        latitude = event.value.toDoubleOrNull()
                    )
                }
            }

            is UpsertRestaurantEvent.OnLongitudeChange -> {
                _state.update {
                    it.copy(
                        restaurantModel = it.restaurantModel?.copy(
                            longitude = event.value.toDoubleOrNull() ?: 0.0
                        ),
                        longitude = event.value.toDoubleOrNull()
                    )
                }
            }

            is UpsertRestaurantEvent.OnNameChange -> {
                _state.update {
                    it.copy(
                        restaurantModel = it.restaurantModel?.copy(
                            name = event.value
                        ),
                        name = event.value
                    )
                }
            }

            is UpsertRestaurantEvent.OnPickPhoto -> {
                viewModelScope.launch {
                    event.photos.forEach { pickPhoto ->
                        editRestaurantRepository.uploadRestaurantImage(
                            pickPhoto.image,
                            _state.value.restaurantModel?.id,
                            pickPhoto.exifOrientation
                        ).onSuccess { uri ->
                            _state.update {
                                it.copy(
                                    restaurantModel = it.restaurantModel?.copy(
                                        photoUris = it.restaurantModel.photoUris + uri
                                    ),
                                    photoUris = it.photoUris + uri
                                )
                            }
                        }
                    }
                }
            }

            is UpsertRestaurantEvent.OnRatingChange -> {
                _state.update {
                    it.copy(
                        restaurantModel = it.restaurantModel?.copy(
                            rating = event.value.toDoubleOrNull()
                        ),
                        rating = event.value.toDoubleOrNull()
                    )
                }
            }

            UpsertRestaurantEvent.OnSave -> {

                val currentState = _state.value
                val restaurantModel = currentState.restaurantModel

                if (restaurantModel != null) {
                    val request = UpdateRestaurantRequest(
                        restaurantId = restaurantModel.id,
                        name = restaurantModel.name,
                        description = restaurantModel.description,
                        photoUris = restaurantModel.photoUris,
                        rating = restaurantModel.rating,
                        averageDeliveryTime = restaurantModel.averageDeliveryTime,
                        deliveryCost = restaurantModel.deliveryCost,
                        addressName = restaurantModel.addressName,
                        latitude = restaurantModel.latitude,
                        longitude = restaurantModel.longitude
                    )

                    viewModelScope.launch {
                        editRestaurantRepository.updateRestaurant(request).onSuccess {
                            baseChannel.send(UpsertRestaurantAction.OnRestaurantAdded)
                        }
                    }
                } else {
                    val request = with(currentState) {
                        AddRestaurantRequest(
                            companyId = "3b4e98cd-6292-4425-9555-225f481eabdd",
                            name = name,
                            description = description,
                            photoUris = photoUris,
                            rating = rating,
                            averageDeliveryTime = averageDeliveryTime,
                            deliveryCost = deliveryCost,
                            addressName = addressName,
                            latitude = latitude ?: 0.0,
                            longitude = longitude ?: 0.0
                        )
                    }

                    viewModelScope.launch {
                        editRestaurantRepository.addRestaurant(request).onSuccess {
                            baseChannel.send(UpsertRestaurantAction.OnRestaurantAdded)
                        }
                    }
                }
            }

            UpsertRestaurantEvent.DeleteRestaurant -> {
                val id: String = navArgs.restaurantId ?: run {
                    baseChannel.trySend(UpsertRestaurantAction.OnRestaurantAdded)
                    return
                }

                viewModelScope.launch {
                    editRestaurantRepository.deleteRestaurant(id).onSuccess {
                        baseChannel.send(UpsertRestaurantAction.OnRestaurantAdded)
                    }
                }
            }
        }
    }

    override fun mapBaseError(message: String): UpsertRestaurantAction {
        return UpsertRestaurantAction.OnError(message)
    }
}