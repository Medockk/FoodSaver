package com.foodsaver.app.data.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.data.dto.OfferDto
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.model.OfferModel
import com.foodsaver.app.domain.repository.OfferRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get

internal class OfferRepositoryImpl(
    private val httpClient: HttpClient
): OfferRepository {

    override suspend fun getOffers(): ApiResult<List<OfferModel>> {
        return saveNetworkCall<List<OfferDto>> {
            httpClient.get(HttpConstants.OFFER_URL)
        }.map { response -> response.map { it.toModel() } }
    }
}