package com.foodsaver.app.coreAddress.domain.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.coreAddress.domain.model.AddAddressModel

interface EditAddressRepository: ReadAddressRepository {

    suspend fun addAddress(addAddressModel: AddAddressModel): ApiResult<Unit>
    suspend fun setCurrentAddress(addressId: String): ApiResult<Unit>
    suspend fun removeAddress(addressId: String): ApiResult<Unit>
}