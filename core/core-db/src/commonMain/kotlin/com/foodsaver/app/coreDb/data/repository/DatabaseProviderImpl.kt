package com.foodsaver.app.coreDb.data.repository

import app.cash.sqldelight.EnumColumnAdapter
import com.databases.cache.CartItemEntity
import com.databases.cache.MainAppDatabase
import com.databases.cache.OrderEntity
import com.databases.cache.PaymentMethodEntity
import com.databases.cache.ProductCacheEntity
import com.databases.cache.RecentKeywordsEntity
import com.databases.cache.RestaurantEntity
import com.databases.cache.UserEntity
import com.foodsaver.app.coreDb.data.adapters.InstantAdapter
import com.foodsaver.app.coreDb.data.adapters.ListOfStringAdapter
import com.foodsaver.app.coreDb.data.adapters.ProductAttributesAdapter
import com.foodsaver.app.coreDb.data.adapters.SyncStatusAdapter
import com.foodsaver.app.coreDb.data.factory.SqlDriverFactory
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider

internal class DatabaseProviderImpl(
    private val sqlDriverFactory: SqlDriverFactory
): DatabaseProvider {

    private var database: MainAppDatabase = lazy {
        val driver = sqlDriverFactory.createSync()
        MainAppDatabase.invoke(
            driver = driver,
            restaurantEntityAdapter = RestaurantEntity.Adapter(
                photoUrisAdapter = ListOfStringAdapter
            ),
            cartItemEntityAdapter = CartItemEntity.Adapter(
                attributesAdapter = ProductAttributesAdapter,
                syncStatusAdapter = SyncStatusAdapter
            ),
            productCacheEntityAdapter = ProductCacheEntity.Adapter(
                expiresAtAdapter = InstantAdapter
            ),
            recentKeywordsEntityAdapter = RecentKeywordsEntity.Adapter(
                addedAtAdapter = InstantAdapter
            ),
            paymentMethodEntityAdapter = PaymentMethodEntity.Adapter(
                expiresDateAdapter = InstantAdapter,
                addedAtAdapter = InstantAdapter
            ),
            orderEntityAdapter = OrderEntity.Adapter(
                typeAdapter = EnumColumnAdapter(),
                statusAdapter = EnumColumnAdapter(),
                createdAtAdapter = InstantAdapter
            ),
            userEntityAdapter = UserEntity.Adapter(
                authoritiesAdapter = ListOfStringAdapter,
                addressIdsAdapter = ListOfStringAdapter
            )
        )
    }.value

    override operator fun invoke(): MainAppDatabase {
        return database
    }
}