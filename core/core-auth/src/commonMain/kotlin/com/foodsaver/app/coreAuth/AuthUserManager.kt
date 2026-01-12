package com.foodsaver.app.coreAuth

expect class AuthUserManager {

    fun getCurrentUid(): String?
    fun setCurrentUid(uid: String)

    fun isUserAuthenticated(): Boolean
    fun logout()
}