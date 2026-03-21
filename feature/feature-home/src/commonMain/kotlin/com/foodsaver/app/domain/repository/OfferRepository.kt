package com.foodsaver.app.domain.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.domain.model.OfferModel

interface OfferRepository {

    suspend fun getOffers(): ApiResult<List<OfferModel>>
}