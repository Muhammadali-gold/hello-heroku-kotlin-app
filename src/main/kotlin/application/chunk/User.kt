package application.chunk

import java.time.Instant

data class User(
    val username: String,
    val screenName: String,
    val email: String,
    val registered: Instant
)