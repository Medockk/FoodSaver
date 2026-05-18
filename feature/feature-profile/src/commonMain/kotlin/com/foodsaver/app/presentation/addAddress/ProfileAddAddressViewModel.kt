package com.foodsaver.app.presentation.addAddress

import androidx.lifecycle.viewModelScope
import com.foodsaver.app.commonModule.presentation.BaseViewModel
import com.foodsaver.app.coreAddress.domain.model.AddAddressRequest
import com.foodsaver.app.coreAddress.domain.repository.EditAddressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileAddAddressViewModel(
    private val addressRepository: EditAddressRepository
): BaseViewModel<ProfileAddAddressAction>() {

    private val _state = MutableStateFlow(ProfileAddAddressState())
    val state = _state.asStateFlow()

    fun onEvent(event: ProfileAddAddressEvent) {
        when (event) {
            is ProfileAddAddressEvent.OnApartmentChange -> {
                _state.update {
                    it.copy(
                        apartment = event.value
                    )
                }
            }
            is ProfileAddAddressEvent.OnFullAddressChange -> {
                _state.update {
                    it.copy(
                        fullAddress = event.value
                    )
                }
            }
            is ProfileAddAddressEvent.OnLabelChange -> {
                _state.update {
                    it.copy(
                        selectedLabel = event.label,
                        labelAsIndex = event.index
                    )
                }
            }
            is ProfileAddAddressEvent.OnPostCodeChange -> {
                _state.update {
                    it.copy(
                        postCode = event.value
                    )
                }
            }
            is ProfileAddAddressEvent.OnStreetChange -> {
                _state.update {
                    it.copy(
                        street = event.value
                    )
                }
            }

            ProfileAddAddressEvent.OnSave -> {
                val currentState = _state.value
                if (currentState.fullAddress.isBlank()) return
                if (currentState.street.isBlank()) return
                if (currentState.postCode.isBlank()) return
                if (currentState.apartment.isBlank()) return

                val request = AddAddressRequest(
                    name = currentState.selectedLabel,
                    latitude = 0.0,
                    longitude = 0.0,
                    city = "Orenburg",
                    street = currentState.street,
                    house = "13",
                    apartment = currentState.apartment,
                    floor = null,
                    entrance = null
                )

                viewModelScope.launch {
                    addressRepository.addAddress(request)
                }
            }
        }
    }

    override fun mapBaseError(message: String): ProfileAddAddressAction {
        return ProfileAddAddressAction.OnError(message)
    }
}