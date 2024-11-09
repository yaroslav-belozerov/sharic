package com.yaabelozerov.sharik.data

import android.util.Log
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("randan")
    suspend fun createRandan(@Body randan: CreateRandanRequest, @Header("Authorization") token: String)

    @GET("user")
    suspend fun getUser(@Header("Authorization") token: String): User

    @GET("userById")
    suspend fun getUserById(@Query("user_id") id: Long,@Header("Authorization") token: String): User

    @GET("randan")
    suspend fun getRandansByUser(@Header("Authorization") token: String
    ): List<Randan>

    @GET("archiveRandans")
    suspend fun getArchiveRandansByUserId(
        @Query("user_id") id: Long, @Header("Authorization") token: String
    ): List<Randan>

    @GET("randanExpenses")
    suspend fun getExpensesByRandanId(
        @Query("randan_id") id: Long, @Header("Authorization") token: String
    ): List<Activity>

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


    @POST("auth/sign-up")
    suspend fun register(@Body registerDTO: RegisterDTO): TokenDTO

    @POST("auth/sign-in")
    suspend fun login(@Body loginDTO: LoginDTO): TokenDTO

    @GET("peopleToGiveMoney")
    suspend fun peopleToGiveMoneyByUser(
        @Query("user_id") id: Long, @Header("Authorization") token: String
    ): List<Pair<User, Float>>

    @GET("peopleToRecieveMoney")
    suspend fun peopleToRecieveMoneyByUser(
        @Query("user_id") id: Long, @Header("Authorization") token: String
    ): List<Pair<User, Float>>

    @POST("addUserToRandan")
    fun addUserToRandan(@Query("randan_id") randanId: Long, @Header("Authorization") token: String)

    @Multipart
    @POST("aws/upload")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part, @Header("Authorization") token: String): ResponseBody

    @PUT("user")
    suspend fun updateUser(@Body user: User, @Header("Authorization") token: String)
}
