package com.foodsaver.app.coreDb.data.repository

import com.databases.cache.MainAppDatabase
import com.databases.cache.RestaurantEntity
import com.databases.cache.UserEntity
import com.foodsaver.app.coreDb.data.adapters.instantAdapter
import com.foodsaver.app.coreDb.data.adapters.listOfStringAdapter
import com.foodsaver.app.coreDb.data.factory.SqlDriverFactory
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class DatabaseProviderImpl(
    private val sqlDriverFactory: SqlDriverFactory
): DatabaseProvider {

    private var database: MainAppDatabase = lazy {
        val driver = sqlDriverFactory.createSync()
        MainAppDatabase.invoke(
            driver = driver,
            userEntityAdapter = UserEntity.Adapter(
                createdAtAdapter = instantAdapter,
                rolesAdapter = listOfStringAdapter
            ),
            restaurantEntityAdapter = RestaurantEntity.Adapter(
                photoUrisAdapter = listOfStringAdapter
            )
        )
    }.value
    private val mutex = Mutex()

    override suspend fun get(): MainAppDatabase {
        return database ?: mutex.withLock {
            database ?: createDatabase()/*.also {
                database = it
            }*/
        }
    }

    override fun getSync(): MainAppDatabase {
        /*if (database == null) {
            val driver = sqlDriverFactory.createSync()
            database = MainAppDatabase.invoke(
                driver = driver,
                cartEntityAdapter = CartEntity.Adapter(
                    productAdapter = ProductColumnAdapter
                ),
                userEntityAdapter = UserEntity.Adapter(
                    createdAtAdapter = instantAdapter,
                    rolesAdapter = listAdapter
                ),
                cachedProductAdapter = CachedProduct.Adapter(
                    productAdapter = ProductColumnAdapter
                )
            )
        }*/

        return database
    }

    private suspend fun createDatabase(): MainAppDatabase {
        val driver = sqlDriverFactory.create()
        return MainAppDatabase.invoke(
            driver = driver,
            userEntityAdapter = UserEntity.Adapter(
                createdAtAdapter = instantAdapter,
                rolesAdapter = listOfStringAdapter
            ),
            restaurantEntityAdapter = RestaurantEntity.Adapter(
                photoUrisAdapter = listOfStringAdapter
            )
        )
    }
}