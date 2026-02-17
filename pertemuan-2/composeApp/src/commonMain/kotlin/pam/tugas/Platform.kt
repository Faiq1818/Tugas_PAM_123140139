package pam.tugas

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform