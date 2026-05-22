package com.foodsaver.app.featurePaymentMethod.data

import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreAuth.requireUserId
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.featurePaymentMethod.domain.OrderRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class OrderRepositoryImpl(
    private val httpClient: HttpClient,
    private val provider: DatabaseProvider,
    private val userManager: AuthUserManager
): OrderRepository {

    private val db by lazy { provider.invoke() }

    override suspend fun makeOrder(): ApiResult<Unit> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<Unit> {
                httpClient.post(HttpConstants.ORDER_URL + "/makeOrder")
            }.onSuccess {
                db.cartItemEntityQueries.clearCartItems(userManager.requireUserId())
                db.cartItemEntityQueries.clearCart(userManager.requireUserId())
            }
        }
    }
}