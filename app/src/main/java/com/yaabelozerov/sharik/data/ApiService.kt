package com.yaabelozerov.sharik.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("currentRandans")
    suspend fun getCurrentRandansByUserId(@Query("user_id") id: Long): List<RandanDTO>

    @GET("archiveRandans")
    suspend fun getArchiveRandansByUserId(@Query("user_id") id: Long): List<RandanDTO>

    @GET("randanExpenses")
    suspend fun getExpensesByRandanId(@Query("randan_id") id: Long): List<ExpenseDTO>
}

class ApiServiceMock : ApiService {
    override suspend fun getCurrentRandansByUserId(id: Long): List<RandanDTO> {
        return listOf(
            RandanDTO(0, "test", listOf(ExpenseDTO(0, "пожрали)))"), ExpenseDTO(1, "поспали))))"))),
            RandanDTO(1, "test 2", listOf(ExpenseDTO(2, "Хайп"))),
        )
    }

    override suspend fun getArchiveRandansByUserId(id: Long): List<RandanDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getExpensesByRandanId(id: Long): List<ExpenseDTO> {
        TODO("Not yet implemented")
    }

}