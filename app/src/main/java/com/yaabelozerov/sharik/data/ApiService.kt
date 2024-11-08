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

    @GET("TotalDebt")
    suspend fun totalDebtByUser(@Query("user_id") id: Long): Float

    @GET("TotalProfit")
    suspend fun totalProfitByUser(@Query("user_id") id: Long): Float

    @GET("peopleToGiveMoney")
    suspend fun peopleToGiveMoneyByUser(@Query("user_id") id: Long): List<Pair<User, Float>>

    @GET("peopleToRecieveMoney")
    suspend fun peopleToRecieveMoneyByUser(@Query("user_id") id: Long): List<Pair<User, Float>>
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

    override suspend fun totalDebtByUser(id: Long): Float {
        return 200f
    }

    override suspend fun totalProfitByUser(id: Long): Float {
        return 500f
    }

    override suspend fun peopleToGiveMoneyByUser(id: Long): List<Pair<User, Float>> {
        return listOf(Pair(User(0, "test_evil", "Mr Evil"), 200f))
    }

    override suspend fun peopleToRecieveMoneyByUser(id: Long): List<Pair<User, Float>> {
        return listOf(Pair(User(0, "test_login", "Mr Test"), 150f), Pair(User(0, "skibidi", "Skibidi Rizzler"), 350f))
    }


}