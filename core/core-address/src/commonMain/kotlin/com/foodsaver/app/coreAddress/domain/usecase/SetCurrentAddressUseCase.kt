package com.foodsaver.app.coreAddress.domain.usecase

import com.foodsaver.app.coreAddress.domain.repository.EditAddressRepository

class SetCurrentAddressUseCase(
    private val editAddressRepository: EditAddressRepository
) {

    suspend operator fun invoke(addressId: String) =
        editAddressRepository.setCurrentAddress(addressId)
}