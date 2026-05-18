package com.foodsaver.app.coreAddress.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreAddress.domain.model.AddAddressRequest

interface EditAddressRepository: ReadAddressRepository {

    suspend fun addAddress(addAddressRequest: AddAddressRequest): ApiResult<Unit>
    suspend fun setCurrentAddress(addressId: String): ApiResult<Unit>
    suspend fun removeAddress(addressId: String): ApiResult<Unit>
}