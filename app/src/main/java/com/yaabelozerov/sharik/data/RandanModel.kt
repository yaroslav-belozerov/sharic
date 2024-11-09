package com.yaabelozerov.sharik.data

import com.squareup.moshi.Json

typealias Randans = List<Randan>;

data class Randan(
    val id: String,
    val name: String,
    val activities: List<Activity>,
    // val debts: List<Debt>,
    val users: List<User>,
    val isFinished: Boolean,
)

data class Activity(
    val id: String,
    val name: String,
    val sum: Long,
    val pays: Pays,
    val randan: String,
    val owes: List<Owe>,
)

data class Pays(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    @Json(name = "avatarURL")
    val avatarUrl: String,
)

data class Owe(
    val user: User,
    val amount: Long,
)

data class User(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    @Json(name = "avatarURL")
    val avatarUrl: String,
)

data class Debt(
    val id: String,
    val user: User,
    val randan: Map<String, Any>,
    val amount: Long,
)

data class User2(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    @Json(name = "avatarURL")
    val avatarUrl: String,
)

data class User3(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    @Json(name = "avatarURL")
    val avatarUrl: String,
)
