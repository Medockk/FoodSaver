package com.foodsaver.app.domain.repository

import com.databases.cache.MainAppDatabase

interface DatabaseProvider {

    suspend fun get(): MainAppDatabase
}