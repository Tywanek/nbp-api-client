package com.radlab.pln

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
