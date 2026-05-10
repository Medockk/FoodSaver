package com.foodsaver.app.data.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.dto.UserDto
import com.foodsaver.app.data.mappers.toDto
import com.foodsaver.app.domain.model.ProfilePersonalInfoModel
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
        TODO()
    }

    override suspend fun uploadAvatar(
        bytes: ByteArray,
        contentType: String,
        fileName: String,
    ): ApiResult<String> {
        TODO()
    }
}