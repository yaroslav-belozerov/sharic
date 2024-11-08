package com.yaabelozerov.sharik.data

data class RandanDTO(val id: Long, val name: String, val expenses: List<ExpenseDTO>)

data class ExpenseDTO(val id: Long, val name: String)

data class User(val id: Long, val login: String, val name: String)