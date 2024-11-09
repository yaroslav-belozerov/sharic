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
    override suspend fun getCurrentRandansByUserId(id: Long, token: String): List<RandanDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getArchiveRandansByUserId(id: Long, token: String): List<RandanDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getExpensesByRandanId(id: Long, token: String): List<ActivityDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun totalDebtByUser(id: Long, token: String): Float {
        return 200f
    }

    override suspend fun totalProfitByUser(id: Long, token: String): Float {
        return 500f
    }

    override suspend fun peopleToGiveMoneyByUser(
        id: Long, token: String
    ): List<Pair<UserDTO, Float>> {
        return listOf(Pair(UserDTO(0, "Mr Evil", "Jonkler", "test_evil"), 200f))
    }

    override suspend fun peopleToRecieveMoneyByUser(
        id: Long, token: String
    ): List<Pair<UserDTO, Float>> {
        return listOf(
            Pair(UserDTO(0, "Mr Test", "Guy", "test_login"), 150f),
            Pair(UserDTO(0, "Skibidi", "Rizzler", "skibidi"), 350f)
        )
    }

    override fun register(registerDTO: RegisterDTO): Call<TokenDTO> {
        TODO("Not yet implemented")
    }

    override fun login(loginDTO: LoginDTO): Call<TokenDTO> {
        TODO("Not yet implemented")
    }
}