package com.example.mypictureoftheday.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate
import java.util.*

interface PictureAPI {
    @GET("planetary/apod")
    fun getPicture (
        @Query("api_key") api_key: String,
        @Query("date") date: LocalDate
    ): Call<PictureDTO>
}