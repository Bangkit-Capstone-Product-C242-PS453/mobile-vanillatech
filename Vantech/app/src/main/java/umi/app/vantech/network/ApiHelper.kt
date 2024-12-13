package umi.app.vantech.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiHelper {

    private const val BASE_URL = "https://api-vanillatech-730442888561.asia-southeast2.run.app/"


    private fun createOkHttpClient(token: String?): OkHttpClient {
        return OkHttpClient.Builder().apply {
            token?.let {
                addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $it")
                        .build()
                    chain.proceed(request)
                }
            }
            connectTimeout(100, TimeUnit.SECONDS)  // Increased timeout for connection
            readTimeout(100, TimeUnit.SECONDS)     // Increased timeout for reading data
            writeTimeout(100, TimeUnit.SECONDS)    // Added write timeout
        }.build()
    }

    fun createApiService(token: String? = null): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient(token))
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun createBaseService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}
