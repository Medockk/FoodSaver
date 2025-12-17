package com.foodsaver.app.data.repository

import app.cash.sqldelight.ColumnAdapter
import com.databases.cache.CartEntity
import com.databases.cache.MainAppDatabase
import com.foodsaver.app.data.adapters.floatAdapter
import com.foodsaver.app.data.adapters.instantAdapter
import com.foodsaver.app.data.adapters.intAdapter
import com.foodsaver.app.data.adapters.listAdapter
import com.foodsaver.app.data.adapters.organizationAdapter
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
                costAdapter = floatAdapter,
                oldCostAdapter = floatAdapter,
                countAdapter = intAdapter,
                ratingAdapter = floatAdapter,
                organizationAdapter = organizationAdapter,
                categoryIdsAdapter = listAdapter,
                expiresAtAdapter = instantAdapter,
                unitAdapter = object : ColumnAdapter<Long, Long> {
                    override fun decode(databaseValue: Long): Long {
                        return databaseValue
                    }

                    override fun encode(value: Long): Long {
                        return value
                    }
                },
                productAdapter = ProductColumnAdapter
            ),
            )
        )

        return database
    }
}