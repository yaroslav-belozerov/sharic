package com.yaabelozerov.sharik.data

data class RandanDTO(val id: Long, val name: String, val expenses: List<ExpenseDTO>)

data class ExpenseDTO(val id: Long, val name: String, val sum: Float, val users: List<Pair<String, Float>>, val paidByUserId: Long)

data class User(val id: Long, val login: String, val name: String)

