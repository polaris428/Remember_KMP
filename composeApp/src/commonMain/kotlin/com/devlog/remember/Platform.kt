package com.devlog.remember

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform