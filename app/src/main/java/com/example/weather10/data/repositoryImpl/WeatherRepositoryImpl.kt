package com.example.weather10.data.repositoryImpl

import android.util.Log
import com.example.weather10.data.remote.HttpRoutes
import com.example.weather10.data.remote.KtorApiClient
import com.example.weather10.data.remote.NetworkResponse
import com.example.weather10.data.remote.model.WeatherResponse
import com.example.weather10.domain.repository.WeatherRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class WeatherRepositoryImpl(private val client: KtorApiClient) : WeatherRepository {

    override suspend fun getCityWeather(city: String): NetworkResponse<WeatherResponse?> {

        return try {
            val response = client.ktorHttpClient.get(HttpRoutes.GET_CITY) {
                contentType(ContentType.Application.Json)
                parameter("key", HttpRoutes.API_KEY)
                parameter("q", city)
            }.body<WeatherResponse>()
            Log.d("TAG", "getCityWeather: response $response ")
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            Log.d("TAG", "getCityWeather: error $e")
            // Handle the error gracefully, e.g., display an error message to the user
            NetworkResponse.Error(e.localizedMessage!!)
        }
    }
}