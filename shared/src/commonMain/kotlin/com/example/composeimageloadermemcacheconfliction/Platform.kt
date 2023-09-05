package com.example.composeimageloadermemcacheconfliction

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform