@file:OptIn(InternalAPI::class)

package com.foodsaver.app.coreProfile.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreAuth.UserNotAuthorizedException
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreProfile.data.dto.ProfileDto
import com.foodsaver.app.coreProfile.data.mappers.mapDtoToEntity
import com.foodsaver.app.coreProfile.data.mappers.mapDtoToResponse
import com.foodsaver.app.coreProfile.data.mappers.mapEntityToModel
import com.foodsaver.app.coreProfile.data.mappers.mapRequestToDto
import com.foodsaver.app.coreProfile.domain.model.ProfileModel
import com.foodsaver.app.coreProfile.domain.model.UpdateProfileRequest
import com.foodsaver.app.coreProfile.domain.repository.ProfileRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class ProfileRepositoryImpl(
    private val httpClient: HttpClient,
    private val provider: DatabaseProvider,
    private val authUserManager: AuthUserManager,
    private val json: Json,
) : ProfileRepository {

    private val db by lazy { provider.invoke() }

    private fun requireUserId() = authUserManager.getCurrentUid()
        ?: throw UserNotAuthorizedException()

    override fun observeProfile(): Flow<ApiResult<ProfileModel>> = channelFlow {
        val userId = requireUserId()

        val databaseJob = launch {
            db.userEntityQueries.getUserById(userId)
                .asFlow()
                .mapToOneOrNull(Dispatchers.InputOutput)
                .collect { user ->
                    user?.let { user ->
                        send(ApiResult.success(user.mapEntityToModel()))
                    }
                }
        }

        awaitClose { databaseJob.cancel() }
    }

    override suspend fun fetchProfile(): ApiResult<ProfileModel> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<ProfileDto> {
                httpClient.get(HttpConstants.PROFILE_URL + "/me")
            }.onSuccess { dto ->
                val entity = dto.mapDtoToEntity()
                db.userEntityQueries.upsertProfile(entity)
            }.map { it.mapDtoToResponse() }
        }
    }

    override suspend fun updateProfile(
        request: UpdateProfileRequest,
        avatar: ByteArray?,
    ) {
        withContext(Dispatchers.InputOutput) {
            saveNetworkCall<ProfileDto> {
                val dto = json.encodeToString(request.mapRequestToDto())
                httpClient.put(HttpConstants.PROFILE_URL + "/update") {
                    setBody(
                        MultiPartFormDataContent(
                            parts = formData {
                                avatar?.let { avatar ->
                                    append("file", avatar, Headers.build {
                                        append(HttpHeaders.ContentType, "image/png")
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=\"avatar.png\""
                                        )
                                    })
                                }
                                append("request", dto, Headers.build {
                                    append(HttpHeaders.ContentType, "application/json")
                                })
                            }
                        )
                    )
                }
            }.onSuccess { dto ->
                db.userEntityQueries.upsertProfile(dto.mapDtoToEntity())
            }
        }
    }
}