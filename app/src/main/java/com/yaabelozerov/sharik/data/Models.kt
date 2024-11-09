package com.yaabelozerov.sharik.data

import com.squareup.moshi.Json


data class RegisterDTO(
    val username: String = "", val password: String = "", val firstName: String = "", val lastName: String = ""
)

data class LoginDTO(val username: String = "", val password: String = "")

data class TokenDTO(val token: String)

data class CreateActivityRequest(
    val name: String,
    val sum: Long,
    val randanId: String,
    val debts: List<DebtRequest>,
)

data class DebtRequest(
    val username: String,
    val amount: Long,
)
