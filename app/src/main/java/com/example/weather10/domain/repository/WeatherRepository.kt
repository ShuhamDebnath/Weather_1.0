package com.example.weather10.domain.repository

import com.example.weather10.data.remote.NetworkResponse
import com.example.weather10.data.remote.model.WeatherResponse
import java.util.concurrent.Flow

interface WeatherRepository {
    suspend fun getCityWeather(city: String): NetworkResponse<WeatherResponse?>
}