package com.foodsaver.app.data.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.map
import com.foodsaver.app.commonModule.ApiResult.onSuccess
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.data.mappers.toDto
import com.foodsaver.app.domain.model.ProfilePersonalInfoModel
import com.foodsaver.app.domain.repository.DatabaseProvider
import com.foodsaver.app.domain.repository.ProfilePersonalInfoRepository
import com.foodsaver.app.dto.UserDto
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

internal class ProfilePersonalInfoRepositoryImpl(
    private val httpClient: HttpClient,
    private val provider: DatabaseProvider,
    private val authUserManager: AuthUserManager
) : ProfilePersonalInfoRepository {

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
                currentCity = it.currentCity
            )
        }.map { }
    }

    override suspend fun uploadAvatar(
        bytes: ByteArray,
        contentType: String,
        fileName: String,
    ): ApiResult<String> {
        return saveNetworkCall<String> {
            httpClient.put(HttpConstants.USER_URL + "/upload-avatar") {
                setBody(
                    MultiPartFormDataContent(
                        parts = formData {
                            append("avatar", bytes, Headers.build {
                                append(HttpHeaders.ContentType, contentType)
                                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                            })
                        }
                    ))
            }
        }.onSuccess { url ->
            val queries = provider.get().usersRequestsQueries
            authUserManager.getCurrentUid()?.let { uid ->
                queries.getUserByUid(uid).executeAsOneOrNull()
                    ?.let { user ->
                        queries.updatePhotoUrl(url, user.uid)
                    }
            }
        }.map {
            it
        }
    }
}