package com.yaabelozerov.sharik.data

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("userById")
    suspend fun getUserById(@Query("user_id") id: Long,@Header("Authorization") token: String): UserDTO

    @GET("currentRandans")
    suspend fun getCurrentRandansByUserId(
        @Query("user_id") id: Long, @Header("Authorization") token: String
    ): List<RandanDTO>

    @GET("archiveRandans")
    suspend fun getArchiveRandansByUserId(
        @Query("user_id") id: Long, @Header("Authorization") token: String
    ): List<RandanDTO>

    @GET("randanExpenses")
    suspend fun getExpensesByRandanId(
        @Query("randan_id") id: Long, @Header("Authorization") token: String
    ): List<ActivityDTO>

    @POST("randanExpensesa")
    suspend fun addExpensesByRandanId(@Query("randan_id") id: Long, @Header("Authorization") token: String)

    @GET("TotalDebt")
    suspend fun totalDebtByUser(
        @Query("user_id") id: Long, @Header("Authorization") token: String
    ): Float

    @GET("TotalProfit")
    suspend fun totalProfitByUser(
        @Query("user_id") id: Long, @Header("Authorization") token: String
    ): Float

    @GET("peopleToGiveMoney")
    suspend fun peopleToGiveMoneyByUser(
        @Query("user_id") id: Long, @Header("Authorization") token: String
    ): List<Pair<UserDTO, Float>>

    @GET("peopleToRecieveMoney")
    suspend fun peopleToRecieveMoneyByUser(
        @Query("user_id") id: Long, @Header("Authorization") token: String
    ): List<Pair<UserDTO, Float>>

    @POST("register")
    fun register(@Body registerDTO: RegisterDTO): Call<TokenDTO>

    @POST("login")
    fun login(@Body loginDTO: LoginDTO): Call<TokenDTO>
}

class ApiServiceMock : ApiService {
    override suspend fun getUserById(id: Long, token: String): UserDTO {
        return UserDTO(
            0,
            "Виктор",
            "Павлович",
            "+788005553535",
        )
    }

    override suspend fun getCurrentRandansByUserId(id: Long, token: String): List<RandanDTO> {
        return listOf(
            RandanDTO(
                0,
                "test",
                listOf(
                    ActivityDTO(
                        0,
                        "Покушали",
                        1000f,
                        listOf(Pair("Петрович", 200f), Pair("Данич", 800f)),
                        0
                    ),
                    ActivityDTO(
                        0,
                        "Ещё раз покушали",
                        1000f,
                        listOf(Pair("Петрович", 200f), Pair("Данич", 700f), Pair("Антон", 100f)),
                        0
                    )
                ),
            )
        )
    }

    override suspend fun getArchiveRandansByUserId(id: Long, token: String): List<RandanDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getExpensesByRandanId(id: Long, token: String): List<ActivityDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun addExpensesByRandanId(id: Long, token: String) {
        TODO("Not yet implemented")
    }

    override suspend fun totalDebtByUser(id: Long, token: String): Float {
        return 200f
    }

    override suspend fun totalProfitByUser(id: Long, token: String): Float {
        return 500f
    }

    override suspend fun peopleToGiveMoneyByUser(id: Long, token: String): List<Pair<UserDTO, Float>> {
        return listOf(Pair(UserDTO(0, "Evil", "Jonkler", "test_evil"), 200f))
    }

    override suspend fun peopleToRecieveMoneyByUser(id: Long, token: String): List<Pair<UserDTO, Float>> {
        return listOf(Pair(UserDTO(0, "Test", "Testovich", "test_login"), 150f), Pair(UserDTO(0, "Skibidi", "Rizzler", "skibidi"), 350f))
    }
    override fun register(registerDTO: RegisterDTO): Call<TokenDTO> {
        TODO("Not yet implemented")
    }
    override fun login(loginDTO: LoginDTO): Call<TokenDTO> {
        TODO("Not yet implemented")
    }
}
