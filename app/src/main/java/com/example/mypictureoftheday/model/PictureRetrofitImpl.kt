package com.example.mypictureoftheday.model

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PictureRetrofitImpl {

    private val baseUrl = "https://api.nasa.gov/"

    fun getRetrofitImpl() : PictureAPI {
        val pictureAPI = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(createOkHttpClient())
            .build()

        return pictureAPI.create(PictureAPI::class.java)
    }

    private fun createOkHttpClient() : OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        return httpClient.build()
    }
}