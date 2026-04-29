package com.foodsaver.app.coreDb.domain.repository

import com.databases.cache.MainAppDatabase

interface DatabaseProvider {

    suspend fun get(): MainAppDatabase
    fun getSync(): MainAppDatabase
}