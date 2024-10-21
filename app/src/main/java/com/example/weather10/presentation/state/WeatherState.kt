package com.example.weather10.presentation.state

import com.example.weather10.data.remote.model.WeatherResponse

data class WeatherState(
    val search: String = "",
    val loading: Boolean = false,
    val error: String = "",
    val weatherResponse: WeatherResponse? = null,
    val tempSearch : String = ""
)