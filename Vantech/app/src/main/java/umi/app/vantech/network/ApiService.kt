package umi.app.vantech.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import umi.app.vantech.data.*

// Retrofit API interface
interface ApiService {

    // Auth
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<TokenResponse>

    @POST("auth/refresh")
    fun refreshToken(@Body request: RefreshTokenRequest): Call<TokenResponse>

    @POST("auth/logout")
    fun logout(): Call<Unit>

    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @GET("user/profile")
    fun getUserProfile(@Header("Authorization") token: String): Call<User>

    // Records
    @GET("records")
    fun getAllRecords(): Call<List<Any>> // Replace `Any` with appropriate data class

    @GET("/records/{id}")
    fun getRecordById(@Path("id") id: Int): Call<DiseaseRecord>


    @DELETE("records/{id}")
    fun deleteRecord(@Path("id") id: Int): Call<Unit>

    // Diseases
    @GET("disease/all")
    fun getAllDiseases(): Call<List<Disease>>

    @GET("disease/{id}")
    fun getDiseaseById(@Path("id") id: Int): Call<Disease>

    // Predictions
    @Multipart
    @POST("/scan")
    fun predictImage(
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Call<PredictionResponse>
}
