package com.foodsaver.app.coreAddress.domain.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.coreModel.model.AddressModel
import kotlinx.coroutines.flow.Flow

interface ReadAddressRepository {

    fun getAddresses(): Flow<ApiResult<List<AddressModel>>>
    fun getCurrentAddress(): Flow<ApiResult<AddressModel?>>
}