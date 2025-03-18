package com.livly.booking.di

import android.util.Log
import com.google.gson.Gson
import com.livly.booking.data.model.ApiResponse
import com.livly.booking.data.remote.AmenityBookingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

open class ApiException(message: String) : IOException(message)
class ServerErrorException(message: String) : ApiException(message)
class ClientErrorException(message: String) : ApiException(message)
class NetworkErrorException : ApiException("Network error occurred. Please check your connection and try again.")

class ErrorInterceptor : Interceptor {
    companion object {
        private const val TAG = "ErrorInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val errorBodyString = response.body?.string()
            Log.e(TAG, "HTTP ${response.code} Error: $errorBodyString")

            val errorMessage = try {
                if (!errorBodyString.isNullOrEmpty()) {
                    val apiResponse = Gson().fromJson(errorBodyString, ApiResponse::class.java)
                    apiResponse.error?.message ?: "Unknown error occurred"
                } else {
                    "Server returned error code: ${response.code}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing error response", e)
                errorBodyString ?: "Unknown error occurred"
            }

            // We need to recreate the response body since we consumed it above
            val contentType = response.body?.contentType()
            val newErrorBody = errorBodyString?.toResponseBody(contentType)

            return response.newBuilder()
                .body(newErrorBody)
                .build()
                .also {
                    when (response.code) {
                        in 400..499 -> throw ClientErrorException(errorMessage)
                        in 500..599 -> throw ServerErrorException(errorMessage)
                        else -> throw ApiException(errorMessage)
                    }
                }
        }

        return response
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
            redactHeader("Authorization")
            redactHeader("Cookie")
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ErrorInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5122/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAmenityBookingService(retrofit: Retrofit): AmenityBookingService {
        return retrofit.create(AmenityBookingService::class.java)
    }
}
