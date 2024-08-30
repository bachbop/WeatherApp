package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Constant
import com.example.weatherapp.api.NetWorkResponse
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.model.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetWorkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetWorkResponse<WeatherModel>> = _weatherResult

    fun fetWeather(lat: String, lon: String) {
        _weatherResult.value = NetWorkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeatherApi(
                    lat = lat,
                    lon = lon,
                    apiKey = Constant.apiKey,
                    units = "metric"
                )

                if (response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value = NetWorkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetWorkResponse.Error("Failed to load data")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetWorkResponse.Error("Failed to load data")
            }
        }
    }

    fun getWeatherByCity(city: String = "hanoi"){
        _weatherResult.value = NetWorkResponse.Loading
        viewModelScope.launch {
            val nameCity = weatherApi.getWeatherByCity(
                city = city,
                apiKey = Constant.apiKey,
                units = "metric"
            )
            if (nameCity.isSuccessful){
                nameCity.body()?.let {
                    _weatherResult.value = NetWorkResponse.Success(it)
                }
            } else {
                _weatherResult.value = NetWorkResponse.Error("Failed to load data")
            }
        }
    }
}