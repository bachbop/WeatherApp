package com.example.weatherapp.model

data class City(
    val coord: Coord,
    val country: String,
    val id: String,
    val name: String,
    val population: String,
    val sunrise: String,
    val sunset: String,
    val timezone: String
)