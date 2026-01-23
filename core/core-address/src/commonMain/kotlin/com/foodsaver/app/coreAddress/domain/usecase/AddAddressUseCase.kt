package com.foodsaver.app.coreAddress.domain.usecase

import com.foodsaver.app.coreAddress.domain.model.AddAddressModel
import com.foodsaver.app.coreAddress.domain.repository.EditAddressRepository

class AddAddressUseCase(
    private val editAddressRepository: EditAddressRepository
) {

    suspend operator fun invoke(addAddressModel: AddAddressModel) =
        editAddressRepository.addAddress(addAddressModel)
}