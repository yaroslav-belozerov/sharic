package com.yaabelozerov.sharik.data

import com.squareup.moshi.Json

data class RandanDTO(val id: Long, val name: String)

data class ActivityDTO(
    val id: Long, val name: String, @Json(name = "randan_id") val randanId: Long
)

data class UserDTO(
    val id: Long,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    val username: String
)

data class RegisterDTO(
    val username: String = "", val firstName: String = "", val lastName: String = "", val password: String = ""
)

data class LoginDTO(val username: String = "", val password: String = "")

data class TokenDTO(val token: String)