package com.foodsaver.app.coreAddress.domain.usecase

import com.foodsaver.app.coreAddress.domain.repository.EditAddressRepository

class RemoveAddressUseCase(
    private val editAddressRepository: EditAddressRepository
) {

    suspend operator fun invoke(addressId: String) =
        editAddressRepository.removeAddress(addressId)
}