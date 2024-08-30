package com.example.weatherapp.api

import com.example.weatherapp.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/forecast")
    suspend fun getWeatherApi(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Response<WeatherModel>

    @GET("data/2.5/forecast")
    suspend fun getWeatherByCity(
        @Query("q")
        city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
    ): Response<WeatherModel>
}