package application.chunk

import com.fasterxml.jackson.annotation.JsonCreator

data class NewUser @JsonCreator constructor(
    val username: String,
    val screenName: String,
    val email: String
)