package com.pertemuan1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform