package com.foodsaver.app.data.repository

import com.databases.cache.CachedProduct
import com.databases.cache.CartEntity
import com.databases.cache.MainAppDatabase
import com.databases.cache.UserEntity
import com.foodsaver.app.data.adapters.ProductColumnAdapter
import com.foodsaver.app.data.adapters.addressColumnAdapter
import com.foodsaver.app.data.adapters.instantAdapter
import com.foodsaver.app.data.adapters.listAdapter
import com.foodsaver.app.data.adapters.paymentCardColumnAdapter
import com.foodsaver.app.data.factory.SqlDriverFactory
import com.foodsaver.app.domain.repository.DatabaseProvider
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class DatabaseProviderImpl(
    private val sqlDriverFactory: SqlDriverFactory
): DatabaseProvider {

    private var database: MainAppDatabase? = null
    private val mutex = Mutex()

    override suspend fun get(): MainAppDatabase {
        return database ?: mutex.withLock {
            database ?: createDatabase().also {
                database = it
            }
        }
    }

    private suspend fun createDatabase(): MainAppDatabase {
        val driver = sqlDriverFactory.create()
        return MainAppDatabase.invoke(
            driver = driver,
            cartEntityAdapter = CartEntity.Adapter(
                productAdapter = ProductColumnAdapter
            ),
            userEntityAdapter = UserEntity.Adapter(
                createdAtAdapter = instantAdapter,
                rolesAdapter = listAdapter,
                addressesAdapter = addressColumnAdapter,
                paymentCartNumbersAdapter = paymentCardColumnAdapter
            ),
            cachedProductAdapter = CachedProduct.Adapter(
                productAdapter = ProductColumnAdapter
            )
        )
    }
}