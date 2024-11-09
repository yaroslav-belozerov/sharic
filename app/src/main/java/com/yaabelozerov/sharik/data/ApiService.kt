package com.yaabelozerov.sharik.data

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("userById")
    suspend fun getUserById(@Query("user_id") id: Long): User

    @GET("currentRandans")
    suspend fun getCurrentRandansByUserId(@Query("user_id") id: Long): List<RandanDTO>

    @GET("archiveRandans")
    suspend fun getArchiveRandansByUserId(@Query("user_id") id: Long): List<RandanDTO>

    @GET("randanExpenses")
    suspend fun getExpensesByRandanId(@Query("randan_id") id: Long): List<ExpenseDTO>

    @POST("randanExpensesa")
    suspend fun addExpensesByRandanId(@Query("randan_id") id: Long)

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
    override suspend fun getUserById(id: Long): User {
        return User(
            0,
            "+788005553535",
            "Анатолий"
        )
    }

    override suspend fun getCurrentRandansByUserId(id: Long): List<RandanDTO> {
        return listOf(
            RandanDTO(
                0,
                "test",
                listOf(
                    ExpenseDTO(
                        0,
                        "пожрали)))",
                        1000f,
                        listOf(Pair("Петрович", 200f), Pair("Данич", 800f)),
                        0
                    ),
                    ExpenseDTO(
                        0,
                        "пожрали)))",
                        1000f,
                        listOf(Pair("Петрович", 200f), Pair("Данич", 800f)),
                        0
                    )
                ),
            )
        )
    }

    override suspend fun getArchiveRandansByUserId(id: Long): List<RandanDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getExpensesByRandanId(id: Long): List<ExpenseDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun addExpensesByRandanId(id: Long) {
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
        return listOf(
            Pair(User(0, "test_login", "Mr Test"), 150f),
            Pair(User(0, "skibidi", "Skibidi Rizzler"), 350f)
        )
    }


}