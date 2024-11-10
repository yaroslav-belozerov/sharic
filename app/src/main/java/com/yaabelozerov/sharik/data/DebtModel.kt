package com.yaabelozerov.sharik.data

import com.squareup.moshi.Json

data class DebtModel(
    val youOweOthers: List<YouOweOther>,
    val othersOweYou: List<OthersOweYou>,
)

data class YouOweOther(
    val who: Who,
    val amount: Long,
)

data class Who(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    @Json(name = "avatarURL")
    val avatarUrl: String,
)

data class OthersOweYou(
    val who: Who,
    val amount: Long,
)
