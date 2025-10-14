package com.foodsaver.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform