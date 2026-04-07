package com.foodsaver.app.featureEnterprises.data.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.featureEnterprises.data.dto.EnterpriseImagesDto
import com.foodsaver.app.featureEnterprises.data.dto.EnterprisesDto
import com.foodsaver.app.featureEnterprises.data.mappers.mapToModel
import com.foodsaver.app.featureEnterprises.domain.model.EnterpriseImagesModel
import com.foodsaver.app.featureEnterprises.domain.model.EnterprisesModel
import com.foodsaver.app.featureEnterprises.domain.model.UploadEnterpriseImageModel
import com.foodsaver.app.featureEnterprises.domain.model.UserLocationModel
import com.foodsaver.app.featureEnterprises.domain.repository.EditEnterpriseRepository
import com.foodsaver.app.featureEnterprises.domain.repository.EnterprisesRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

internal class EnterprisesRepositoryImpl(
    private val httpClient: HttpClient,
) : EnterprisesRepository, EditEnterpriseRepository {

    override suspend fun getNearestEnterprises(userLocationModel: UserLocationModel): ApiResult<List<EnterprisesModel>> {
        return saveNetworkCall<List<EnterprisesDto>> {
            httpClient.get(HttpConstants.ENTERPRISE_URL) {
                parameter("latitude", userLocationModel.latitude)
                parameter("longitude", userLocationModel.longitude)
            }
        }.map { enterprises ->
            enterprises.map { it.mapToModel() }
        }
    }

    override suspend fun getEnterpriseById(enterpriseId: String): ApiResult<EnterprisesModel?> {
        val response: ApiResult<EnterprisesDto?> = saveNetworkCall {
            httpClient.get(HttpConstants.ENTERPRISE_URL + "/$enterpriseId")
        }
        return response.map { it?.mapToModel() }
    }

    override suspend fun getEnterpriseImageUrls(enterpriseId: String): ApiResult<List<EnterpriseImagesModel>> {
        return saveNetworkCall<List<EnterpriseImagesDto>> {
            httpClient.get(HttpConstants.ENTERPRISE_URL + "/images") {
                parameter("enterpriseId", enterpriseId)
            }
        }.map { dtos ->
            dtos.map {
                EnterpriseImagesModel(it.imageUrl)
            }
        }
    }

    override suspend fun uploadEnterpriseImage(uploadEnterpriseImageModel: UploadEnterpriseImageModel): ApiResult<String?> {
        return saveNetworkCall {
            httpClient.post(HttpConstants.ENTERPRISE_URL + "/uploadImage") {
                setBody(
                    MultiPartFormDataContent(
                        parts = formData {
                            append("file", uploadEnterpriseImageModel.image, Headers.build {
                                append(HttpHeaders.ContentType, uploadEnterpriseImageModel.mimeType)
                                append(
                                    HttpHeaders.ContentDisposition,
                                    "filename=\"enterprise.jpg\""
                                )
                            })
                        }
                    ))

                parameter("enterpriseId", uploadEnterpriseImageModel.enterpriseId)
            }
        }
    }
}