package com.example.weather10.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weather10.WeatherApplication
import com.example.weather10.data.remote.NetworkResponse
import com.example.weather10.data.remote.model.WeatherResponse
import com.example.weather10.data.repositoryImpl.WeatherRepositoryImpl
import com.example.weather10.domain.repository.WeatherRepository
import com.example.weather10.presentation.event.WeatherEvent
import com.example.weather10.presentation.state.WeatherState
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState = _weatherState.asStateFlow()


    private fun clearWeatherAndErrorState() {
        _weatherState.update {
            it.copy(
                error = "",
                weatherResponse = null
            )
        }
    }

    private fun loading(loading: Boolean) {
        _weatherState.update {
            it.copy(
                loading = loading,
            )
        }
    }

    private suspend fun loadWeatherDetails(city: String) {

        loading(true)
        clearWeatherAndErrorState()

        repository.getCityWeather(city = city).also { response ->
            Log.d("TAG", "onWeatherEvent: response $response ")
            when (response) {
                is NetworkResponse.Error -> {
                    _weatherState.update {
                        it.copy(
                            loading = false,
                            error = response.errorMessage,
                            weatherResponse = null
                        )
                    }
                }

                NetworkResponse.Loading -> {
                    loading(true)
                }

                is NetworkResponse.Success -> {
                    Log.d("TAG", "onWeatherEvent: response $response")
                    _weatherState.update {
                        it.copy(
                            loading = false,
                            error = "",
                            weatherResponse = response.data,
                            tempSearch = it.search
                        )
                    }
                }
            }
        }
    }


    fun onWeatherEvent(event: WeatherEvent) {
        when (event) {
            WeatherEvent.OnSearchClicked -> {
                Log.d("TAG", "onWeatherEvent: WeatherEvent.OnSearchClicked")
                viewModelScope.launch {
                    loadWeatherDetails(_weatherState.value.search)
                }
            }

            is WeatherEvent.OnSearchUpdate -> {
                _weatherState.update {
                    it.copy(
                        search = event.search
                    )
                }
            }

            WeatherEvent.OnRefreshClicked -> {
                viewModelScope.launch {
                    loadWeatherDetails(_weatherState.value.tempSearch)
                }
            }
        }
    }

}