package com.foodsaver.app.data.repository

import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.ApiResult.map
import com.foodsaver.app.ApiResult.onSuccess
import com.foodsaver.app.data.mappers.toDto
import com.foodsaver.app.domain.model.ProfilePersonalInfoModel
import com.foodsaver.app.domain.repository.DatabaseProvider
import com.foodsaver.app.domain.repository.ProfilePersonalInfoRepository
import com.foodsaver.app.dto.UserDto
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.request.setBody

internal class ProfilePersonalInfoRepositoryImpl(
    private val httpClient: HttpClient,
    private val provider: DatabaseProvider
): ProfilePersonalInfoRepository {

    override suspend fun save(profilePersonalInfoModel: ProfilePersonalInfoModel): ApiResult<Unit> {
        return saveNetworkCall<UserDto> {
            httpClient.put(HttpConstants.USER_URL) {
                setBody(profilePersonalInfoModel.toDto())
            }
        }.onSuccess {
            val queries = provider.get().usersRequestsQueries
            queries.updateUser(
                name = it.name,
                email = it.email,
                photoUrl = it.photoUrl,
                bio = it.bio,
                phone = it.phone,
                addresses = it.addresses,
                paymentCartNumbers = it.paymentCartNumbers,

                uid = it.uid,
            )
        }.map {  }
    }
}