package com.example.weatherapp.model

data class WeatherList(
    val dt : String,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: String,
    val pop: String,
    val rain: Rain,
    val sys: Sys,
    val dtTxt: String
)