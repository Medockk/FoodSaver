package com.foodsaver.app.coreAddress.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreAddress.domain.model.AddressModel
import kotlinx.coroutines.flow.Flow

interface ReadAddressRepository {

    fun observeUserAddresses(): Flow<ApiResult<List<AddressModel>>>
    fun observeCurrentUserAddress(): Flow<ApiResult<AddressModel?>>
}