package com.foodsaver.app.coreAddress.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreModel.model.AddressModel
import kotlinx.coroutines.flow.Flow

interface ReadAddressRepository {

    fun observeAddresses(): Flow<ApiResult<List<AddressModel>?>>
    fun observeCurrentAddress(): Flow<ApiResult<AddressModel?>>
}