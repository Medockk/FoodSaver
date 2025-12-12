package com.foodsaver.app.data.mappers

import com.databases.cache.Users
import com.foodsaver.app.domain.model.UserModel

fun Users.toModel() =
    UserModel(uid, name)