package com.foodsaver.app.coreAddress.domain.usecase

import com.foodsaver.app.coreAddress.domain.model.AddAddressRequest
import com.foodsaver.app.coreAddress.domain.repository.EditAddressRepository

class AddAddressUseCase(
    private val editAddressRepository: EditAddressRepository
) {

    suspend operator fun invoke(addAddressRequest: AddAddressRequest) =
        editAddressRepository.addAddress(addAddressRequest)
}