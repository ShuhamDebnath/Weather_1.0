package com.example.weather10.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("current")
    val current: Current,
    @SerialName("location")
    val location: Location
)