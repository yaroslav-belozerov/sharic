package com.yaabelozerov.sharik.data

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("currentRandans")
    suspend fun getCurrentRandansByUserId(@Query("user_id") id: Long): List<RandanDTO>

    @GET("archiveRandans")
    suspend fun getArchiveRandansByUserId(@Query("user_id") id: Long): List<RandanDTO>

    @GET("randanExpenses")
    suspend fun getExpensesByRandanId(@Query("randan_id") id: Long): List<ActivityDTO>

    @GET("TotalDebt")
    suspend fun totalDebtByUser(@Query("user_id") id: Long): Float

    @GET("TotalProfit")
    suspend fun totalProfitByUser(@Query("user_id") id: Long): Float

    @GET("peopleToGiveMoney")
    suspend fun peopleToGiveMoneyByUser(@Query("user_id") id: Long): List<Pair<UserDTO, Float>>

    @GET("peopleToRecieveMoney")
    suspend fun peopleToRecieveMoneyByUser(@Query("user_id") id: Long): List<Pair<UserDTO, Float>>

    @POST("register")
    fun register(@Body registerDTO: RegisterDTO): Call<String>

    @POST("login")
    fun login(@Body loginDTO: LoginDTO): Call<String>
}

class ApiServiceMock : ApiService {
    override suspend fun getCurrentRandansByUserId(id: Long): List<RandanDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getArchiveRandansByUserId(id: Long): List<RandanDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getExpensesByRandanId(id: Long): List<ActivityDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun totalDebtByUser(id: Long): Float {
        return 200f
    }

    override suspend fun totalProfitByUser(id: Long): Float {
        return 500f
    }

    override suspend fun peopleToGiveMoneyByUser(id: Long): List<Pair<UserDTO, Float>> {
        return listOf(Pair(UserDTO(0, "Mr Evil", "Jonkler", "test_evil"), 200f))
    }

    override suspend fun peopleToRecieveMoneyByUser(id: Long): List<Pair<UserDTO, Float>> {
        return listOf(
            Pair(UserDTO(0, "Mr Test", "Guy", "test_login"), 150f),
            Pair(UserDTO(0, "Skibidi", "Rizzler", "skibidi"), 350f)
        )
    }

    override fun register(registerDTO: RegisterDTO): Call<String> {
        TODO("Not yet implemented")
    }

    override fun login(loginDTO: LoginDTO): Call<String> {
        TODO("Not yet implemented")
    }


}