package com.yaabelozerov.sharik.data

import com.squareup.moshi.Json

data class RandanDTO(val id: Long, val name: String, val activities: List<ActivityDTO>)

data class ActivityDTO(
    val id: Long, val name: String, val sum: Float, val users: List<Pair<String, Float>>, val paidByUserId: Long
)

data class UserDTO(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val username: String
)

data class RegisterDTO(
    val username: String = "", val password: String = "", val firstName: String = "", val lastName: String = ""
)

data class LoginDTO(val username: String = "", val password: String = "")

data class TokenDTO(val token: String)