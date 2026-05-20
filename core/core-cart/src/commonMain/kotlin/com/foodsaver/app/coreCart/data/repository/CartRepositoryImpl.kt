@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.coreCart.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreAuth.UserNotAuthorizedException
import com.foodsaver.app.coreCart.data.dto.CartItemDto
import com.foodsaver.app.coreCart.data.dto.CartResponseDto
import com.foodsaver.app.coreCart.data.dto.ProductInCartResponseDto
import com.foodsaver.app.coreCart.data.mappers.mapCartEntityToModel
import com.foodsaver.app.coreCart.data.mappers.mapDtoToEntity
import com.foodsaver.app.coreCart.data.mappers.mapDtoToModel
import com.foodsaver.app.coreCart.data.mappers.mapModelToDto
import com.foodsaver.app.coreCart.domain.model.AddProductToCartRequestModel
import com.foodsaver.app.coreCart.domain.model.CartItemAttributes
import com.foodsaver.app.coreCart.domain.model.CartItemModel
import com.foodsaver.app.coreCart.domain.model.CartResponseModel
import com.foodsaver.app.coreCart.domain.model.ChangeQuantityRequest
import com.foodsaver.app.coreCart.domain.model.DeleteCartItemRequestModel
import com.foodsaver.app.coreCart.domain.model.ProductInCartResponseModel
import com.foodsaver.app.coreCart.domain.repository.CartRepository
import com.foodsaver.app.coreDb.domain.model.ProductAttributes
import com.foodsaver.app.coreDb.domain.model.SyncStatus
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class CartRepositoryImpl(
    databaseProvider: DatabaseProvider,
    private val httpClient: HttpClient,
    private val authUserManager: AuthUserManager,
) : CartRepository {

    private val db = databaseProvider()

    private fun requireUserId() =
        authUserManager.getCurrentUid() ?: throw UserNotAuthorizedException()

    override suspend fun observeCart(): Flow<ApiResult<CartResponseModel>> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext channelFlow {
                val userId = requireUserId()

                // getting cart from local database
                val databaseJob = launch {
                    db.cartEntityQueries.getCartByUserId(userId)
                        .asFlow()
                        .mapToOneOrNull(Dispatchers.InputOutput)
                        .collect { entity ->
                            entity?.let { entity ->
                                send(ApiResult.success(entity.mapCartEntityToModel()))
                            }
                        }
                }

                // get cart from server
                println("Отправляю запрос на получение корзины")
                val networkResponse = saveNetworkCall<CartResponseDto> {
                    httpClient.get(HttpConstants.CART_URL + "/my")
                }.onSuccess { response: CartResponseDto ->
                    // add server response to local database
                    println("Получил ответ от сервера для получения корзины")
                    db.cartEntityQueries.insertOrUpdateCart(
                        id = response.id,
                        userId = userId,
                        totalQuantity = response.quantity,
                        price = response.finalPrice
                    )
                }.map { dto -> dto.mapDtoToModel() }

                send(networkResponse)

                awaitClose {
                    databaseJob.cancel()
                }
            }
        }
    }

    override suspend fun observeCartItems(cartId: String): Flow<ApiResult<List<CartItemModel>>> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext channelFlow {
                val userId = requireUserId()

                // launch sync task
                launch {
                    syncPendingProducts()
                }

                // parallel subscribe to database
                val databaseJob = launch {
                    db.cartItemEntityQueries
                        .getCartWithProductDetails(userId)
                        .asFlow()
                        .mapToList(Dispatchers.InputOutput)
                        .collect { products ->
                            println("Получил новые значения из локальной базы")
                            println("Новые значения для ${products.map { it.serverId }}")
                            db.cartEntityQueries.updateCount(
                                totalQuantity = products.size.toLong(),
                                userId = userId
                            )
                            val cartItems = products.map {
                                CartItemModel(
                                    localId = it.localId,
                                    serverId = it.serverId,
                                    productId = it.productId,
                                    name = it.name,
                                    price = it.price,
                                    currency = it.currency,
                                    imageUri = it.imageUris?.firstOrNull(),
                                    quantity = it.quantity,
                                    attributes = CartItemAttributes(
                                        size = it.attributes?.size,
                                        additions = it.attributes?.additions ?: emptyList()
                                    )
                                )
                            }

                            send(ApiResult.success(cartItems))
                        }
                }

                // parallel send HTTP request to server
                println("Отправляю запрос на сервер для получение элементов корзины")
                saveNetworkCall<List<CartItemDto>> {
                    httpClient.get(HttpConstants.CART_URL + "/items") {
                        parameter("cartId", cartId)
                    }
                }.onSuccess { items ->
                    println(
                        "Получил ответ от сервера на получение элементов корзины\n" +
                                "Результат: ${items.map { it.productId }}"
                    )

                    // deleting old values
                    val networkCartItemIds = items.map { it.cartItemId }
                    db.cartItemEntityQueries.transaction {
                        if (networkCartItemIds.isEmpty()) {
                            println("Удаляю все синхронизированные продукты")
                            db.cartItemEntityQueries.deleteAllSynchronizedByUserId(userId)
                        } else {
                            println("Удаляю все старые синхронизированные продукты")
                            db.cartItemEntityQueries.deleteOldSynchronizedItems(
                                userId,
                                networkCartItemIds
                            )
                        }
                    }

                    // getting new values
                    val networkProductIds = items.map { it.productId }.distinct()
                    val cachedProductIds = db.productEntityQueries
                        .getExistingIds(networkProductIds)
                        .executeAsList()
                    val missingProductIds = networkProductIds
                        .filterNot { it in cachedProductIds }

                    if (missingProductIds.isNotEmpty()) {
                        println("Продукты $missingProductIds отсутствуют в локальной БД. Делаю запрос на сервер")
                        saveNetworkCall<List<ProductDto>> {
                            httpClient.get(HttpConstants.PRODUCTS_URL + "/ids") {
                                missingProductIds.forEach {
                                    parameter("ids", it)
                                }
                            }
                        }.onSuccess { productDtos ->
                            println("Успешно получил пропущенные продукты $missingProductIds с сервера")
                            db.productEntityQueries.transaction {
                                productDtos.forEach { dto ->
                                    db.productEntityQueries.insertProduct(dto.mapDtoToEntity())
                                }
                                println("Сохранил пропущенные продукты $missingProductIds в локальную БД")
                            }
                        }
                    }

                    db.cartItemEntityQueries.transaction {
                        items.forEach { item ->

                            // Проверяем, нет ли уже такого товара локально с пометкой isDeleted
                            val existing =
                                db.cartItemEntityQueries.getCartItemByServerId(item.cartItemId)
                                    .executeAsOneOrNull()

                            if (existing?.isDeleted == true) {
                                return@forEach
                            }

                            db.cartItemEntityQueries
                                .upsertCartItem(
                                    localId = existing?.localId ?: Uuid.random().toString(),
                                    serverId = item.cartItemId,
                                    productId = item.productId,
                                    cartId = cartId,
                                    userId = userId,
                                    quantity = item.quantity,
                                    attributes = ProductAttributes(
                                        size = item.attributes?.size,
                                        additions = item.attributes?.additions ?: emptyList()
                                    ),
                                    addedAt = Clock.System.now().toString(),
                                    syncStatus = SyncStatus.SYNCHRONIZED
                                )
                        }
                    }
                }.onFailure {
                    send(it)
                }

                awaitClose {
                    databaseJob.cancel()
                }
            }
        }
    }

    override suspend fun addProductToCart(request: AddProductToCartRequestModel): ApiResult<CartItemModel> {
        return withContext(Dispatchers.InputOutput) {

            val userId = requireUserId()
            val cartId = db.cartEntityQueries.getCartByUserId(userId)
                .executeAsOneOrNull()?.id
            var cachedProduct = db.productEntityQueries.getProduct(
                productId = request.productId
            ).executeAsOneOrNull()
            val attributes = ProductAttributes(
                size = request.attributes.size,
                additions = request.attributes.additions
            )

            // add to local database
            launch {

                cartId ?: return@launch
                cachedProduct?.let { cachedProduct ->
                    println("Добавляю продукт в локальную корзину")
                    db.cartItemEntityQueries.upsertCartItem(
                        localId = Uuid.random().toString(),
                        serverId = null,
                        productId = cachedProduct.productId,
                        cartId = cartId,
                        userId = userId,
                        quantity = request.quantity ?: 1L,
                        attributes = attributes,
                        addedAt = Clock.System.now().toString(),
                        syncStatus = SyncStatus.PENDING
                    )
                }

            }

            val localCartItemId = Uuid.random().toString()
            val networkResult = saveNetworkCall<CartItemDto> {
                httpClient.post(HttpConstants.CART_URL + "/add") {
                    setBody(request.mapModelToDto())
                }
            }.onSuccess { dto ->
                cartId?.let { cartId ->
                    println("Успешно добавил продукт ${request.productId} на сервер")
                    db.cartItemEntityQueries.upsertCartItem(
                        localId = localCartItemId,
                        serverId = dto.cartItemId,
                        productId = dto.productId,
                        cartId = cartId,
                        userId = userId,
                        quantity = dto.quantity,
                        attributes = attributes,
                        addedAt = Clock.System.now().toString(),
                        syncStatus = SyncStatus.SYNCHRONIZED
                    )
                    println("Успешно сохранил продукт ${request.productId} в локальную бд")

                    db.cartEntityQueries.getCartByUserId(userId)
                        .executeAsOneOrNull()
                        ?.let { cart ->
                            db.cartEntityQueries.updateCount(cart.totalQuantity + 1L, userId)
                        }
                }
            }.map { dto ->

                if (cachedProduct == null) {
                    println("Продукт ${request.productId} в локальной БД отсутствует. Делаю запрос на сервер")
                    saveNetworkCall<ProductDto> {
                        httpClient.get(HttpConstants.PRODUCTS_URL + "/id") {
                            parameter("productId", dto.productId)
                        }
                    }.onSuccess {
                        db.productEntityQueries.insertProduct(it.mapDtoToEntity())
                        cachedProduct = db.productEntityQueries.getProduct(it.productId)
                            .executeAsOneOrNull()
                        println("Сохранил продукт в локальную БД")
                    }
                }

                cartId?.let {
                    println("Сохраняю продукт ${dto.productId} в корзину с идентификатором ${dto.cartItemId}")
                    db.cartItemEntityQueries.upsertCartItem(
                        localId = localCartItemId,
                        serverId = dto.cartItemId,
                        productId = dto.productId,
                        cartId = cartId,
                        userId = userId,
                        quantity = dto.quantity,
                        attributes = attributes,
                        addedAt = Clock.System.now().toString(),
                        syncStatus = SyncStatus.SYNCHRONIZED
                    )
                }

                dto.mapDtoToModel(
                    localId = localCartItemId,
                    name = cachedProduct?.name ?: "",
                    price = cachedProduct?.price ?: 0.0,
                    currency = cachedProduct?.currency ?: "",
                    imageUri = cachedProduct?.imageUris?.firstOrNull() ?: "",
                    quantity = dto.quantity
                )
            }

            return@withContext networkResult
        }
    }

    override suspend fun changeProductQuantity(request: ChangeQuantityRequest): ApiResult<Unit> {
        return withContext(Dispatchers.InputOutput) {

            // upate count into local database
            launch {
                println("Обновляю значение элемента корзины ${request.cartItemId} на значение ${request.newQuantity}")
                // after update observe function automatically send new value
                db.cartItemEntityQueries.changeQuantity(
                    quantity = request.newQuantity,
                    serverId = request.cartItemId,
                    localId = request.localId
                )
            }

            return@withContext saveNetworkCall<CartItemDto> {
                httpClient.put(HttpConstants.CART_URL + "/changeQuantity") {
                    setBody(request.mapModelToDto())
                }
            }.map { }
        }
    }

    override suspend fun observeCartProductIds(): Flow<ApiResult<List<ProductInCartResponseModel>>> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext channelFlow {
                val userId = requireUserId()

                val databaseJob = launch {
                    db.cartItemEntityQueries.getCartWithProductDetails(userId)
                        .asFlow()
                        .mapToList(Dispatchers.InputOutput)
                        .collect { items ->
                            val productIds = items.map {
                                ProductInCartResponseModel(
                                    productId = it.productId,
                                    cartItemId = it.serverId
                                )
                            }
                            send(ApiResult.success(productIds))
                        }
                }

                val networkResult = saveNetworkCall<List<ProductInCartResponseDto>> {
                    httpClient.get(HttpConstants.CART_URL + "/myIds")
                }.map { dtos ->
                    dtos.map {
                        ProductInCartResponseModel(
                            productId = it.productId,
                            cartItemId = it.cartItemId
                        )
                    }
                }
                send(networkResult)

                awaitClose { databaseJob.cancel() }
            }
        }
    }

    override suspend fun syncPendingProducts() {
        withContext(Dispatchers.InputOutput) {
            val pendingProducts = db.cartItemEntityQueries
                .getPendingItems()
                .executeAsList()

            pendingProducts.forEach { product ->
                val addProductRequest = AddProductToCartRequestModel(
                    productId = product.productId,
                    quantity = product.quantity,
                    attributes = CartItemAttributes(
                        size = product.attributes?.size,
                        additions = product.attributes?.additions
                    )
                )

                saveNetworkCall<CartItemDto> {
                    httpClient.post(HttpConstants.CART_URL + "/add") {
                        setBody(addProductRequest.mapModelToDto())
                    }
                }.onSuccess { dto ->
                    println("Успешно синхронизировал продукт ${addProductRequest.productId} с сервером")
                    db.cartItemEntityQueries.changeSyncStatus(
                        serverId = dto.cartItemId,
                        syncStatus = SyncStatus.SYNCHRONIZED,
                        localId = product.localId
                    )
                }
            }

            val deletedProducts = db.cartItemEntityQueries.getDeletedPendingItems()
                .executeAsList()
            deletedProducts.forEach { product ->
                val serverId = product.serverId ?: return@forEach

                saveNetworkCall<Unit> {
                    httpClient.delete(HttpConstants.CART_URL + "/delete") {
                        parameter("id", serverId)
                    }
                }.onSuccess {
                    println("Успешно синхронизировал удалённые продукты")
                    db.cartItemEntityQueries.delete(
                        serverId = product.serverId,
                        localId = product.localId
                    )
                }
            }
        }
    }

    override suspend fun removeProductFromCart(request: DeleteCartItemRequestModel): ApiResult<Unit> {
        return withContext(Dispatchers.InputOutput) {

            // try to delete from local database
            db.cartItemEntityQueries.markAsDeleted(
                serverId = request.cartItemId,
                localId = request.localId
            )
            println("Элемент корзины ${request.cartItemId} помечен на удаление")

            if (request.cartItemId == null) {
                db.cartItemEntityQueries.delete(null, request.localId)
                println("Элемент корзины с локальным ID ${request.localId} удалён, т.к. его нет на сервере")
                return@withContext ApiResult.success(Unit)
            }

            return@withContext request.cartItemId.let {
                return@let saveNetworkCall<Unit?> {
                    httpClient.delete(HttpConstants.CART_URL + "/delete") {
                        parameter("id", request.cartItemId)
                    }
                }.onSuccess {
                    db.cartItemEntityQueries.delete(
                        serverId = request.cartItemId,
                        localId = request.localId
                    )
                    println("Элемент корзины ${request.cartItemId} удалён с сервера и из локальной БД")
                }.map { }
            }
        }
    }
}