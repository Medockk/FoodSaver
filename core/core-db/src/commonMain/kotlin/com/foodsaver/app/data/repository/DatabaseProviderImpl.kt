package com.foodsaver.app.data.repository

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

internal class DatabaseProviderImpl(
    private val sqlDriverFactory: SqlDriverFactory
): DatabaseProvider {

    override suspend fun get(): MainAppDatabase {
        val driver = sqlDriverFactory.create()

        val database = MainAppDatabase.invoke(
            driver = driver,
            cartEntityAdapter = CartEntity.Adapter(
                productAdapter = ProductColumnAdapter
            ),
            userEntityAdapter = UserEntity.Adapter(
                createdAtAdapter = instantAdapter,
                rolesAdapter = listAdapter,
                addressesAdapter = addressColumnAdapter,
                paymentCartNumbersAdapter = paymentCardColumnAdapter
            )
        )

        return database
    }
}