package com.foodsaver.app.coreDb.domain.repository

import com.databases.cache.MainAppDatabase

interface DatabaseProvider {

    operator fun invoke(): MainAppDatabase
}