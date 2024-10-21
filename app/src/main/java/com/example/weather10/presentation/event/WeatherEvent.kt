package com.example.weather10.presentation.event

sealed interface WeatherEvent {
    data class OnSearchUpdate(val search :String) : WeatherEvent
    data object OnSearchClicked : WeatherEvent
    data object OnRefreshClicked : WeatherEvent
}