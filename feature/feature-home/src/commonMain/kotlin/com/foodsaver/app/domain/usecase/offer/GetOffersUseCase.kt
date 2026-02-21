package com.foodsaver.app.domain.usecase.offer

import com.foodsaver.app.domain.repository.OfferRepository

class GetOffersUseCase(
    private val repository: OfferRepository
) {

    suspend operator fun invoke() =
        repository.getOffers()
}