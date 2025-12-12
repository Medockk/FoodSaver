@file:OptIn(DelicateCoroutinesApi::class)

package com.foodsaver.app.data.repository

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import com.databases.cache.MainAppDatabase
import com.foodsaver.app.data.factory.SqlDriverFactory
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.repository.UserDatabaseApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

internal class UserDatabaseApiImpl(
    private val sqlDriverFactory: SqlDriverFactory
): UserDatabaseApi {

    private val databaseDeferred = CoroutineScope(Dispatchers.Default).async {
        val driver = sqlDriverFactory.create()
        MainAppDatabase.invoke(driver)
    }
    private suspend fun usersRequestsQueries() = databaseDeferred.await().usersRequestsQueries

    override suspend fun getAllUsers(): List<UserModel> {
        return usersRequestsQueries().getAllUsers().awaitAsList().map {
            it.toModel()
        }
    }

    override suspend fun insertUser(userModel: UserModel): UserModel {
        usersRequestsQueries().insertUser(userModel.uid, userModel.name)
        return userModel
    }

    override suspend fun getUserByUid(uid: String): UserModel? {
        return usersRequestsQueries().getUserByUid(uid).awaitAsOneOrNull()?.toModel()
    }

}
