package com.foodsaver.app.data.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.map
import com.foodsaver.app.commonModule.ApiResult.onSuccess
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreModel.dto.UserDto
import com.foodsaver.app.data.mappers.toDto
import com.foodsaver.app.domain.model.ProfilePersonalInfoModel
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.domain.repository.ProfilePersonalInfoRepository
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
            val database = provider.get()

            val userEntityQueries = database.userEntityQueries
            userEntityQueries.updateUser(
                name = it.name,
                email = it.email,
                photoUrl = it.photoUrl,
                bio = it.bio,
                phone = it.phone,
                uid = it.uid
            )

            val addressEntityQueries = database.addressEntityQueries
            it.addresses.forEach { addressDto ->
                addressEntityQueries.updateAddress(
                    name = addressDto.name,
                    address = addressDto.address,
                    uid = it.uid,
                    globalId = addressDto.id,
                    isCurrentAddress = addressDto.isCurrentAddress,
                )
            }
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
            val queries = provider.get().userEntityQueries
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