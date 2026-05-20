package com.foodsaver.app.addProductModule.data.repository

import com.foodsaver.app.addProductModule.data.dto.UploadImageDto
import com.foodsaver.app.addProductModule.data.mappers.mapDtoToModel
import com.foodsaver.app.addProductModule.data.mappers.mapRequestToDto
import com.foodsaver.app.addProductModule.domain.model.AddProductRequest
import com.foodsaver.app.addProductModule.domain.model.UploadImageModel
import com.foodsaver.app.addProductModule.domain.repository.AddProductRepository
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.utils.image.ImageCompressor
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreAuth.requireUserId
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.mappers.toModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AddProductRepositoryImpl(
    private val httpClient: HttpClient,
    private val authUserManager: AuthUserManager,
    private val provider: DatabaseProvider
): AddProductRepository {

    private val db by lazy { provider() }

    override suspend fun uploadImage(image: ByteArray): ApiResult<UploadImageModel> {
        return withContext(Dispatchers.InputOutput) {
            val image = ImageCompressor.compress(image)
            saveNetworkCall<UploadImageDto> {
                httpClient.post(HttpConstants.PRODUCTS_URL + "/uploadImage") {
                    setBody(
                        MultiPartFormDataContent(
                            parts = formData {
                                append("image", image, Headers.build {
                                    append(HttpHeaders.ContentType, "image/png")
                                    append(HttpHeaders.ContentDisposition, "filename=\"productImage.png\"")
                                })
                            }
                        )
                    )
                }
            }.map { it.mapDtoToModel() }
        }
    }

    override suspend fun addProduct(request: AddProductRequest): ApiResult<ProductModel> {
        return withContext(Dispatchers.InputOutput) {
            val userId = authUserManager.requireUserId()
            val user = db.userEntityQueries.getUserById(userId).executeAsOneOrNull()
                ?: return@withContext ApiResult.error("User not found!")

            if (user.restaurantId == null) return@withContext ApiResult.error("User don't have authority!")

            saveNetworkCall<ProductDto> {
                httpClient.post(HttpConstants.PRODUCTS_URL + "/add") {
                    setBody(request.mapRequestToDto(user.restaurantId!!))
                }
            }.map { it.toModel() }
        }
    }

    override suspend fun fetchCurrencies(): ApiResult<List<String>> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall {
                httpClient.get(HttpConstants.PRODUCTS_URL + "/currencies")
            }
        }
    }
}