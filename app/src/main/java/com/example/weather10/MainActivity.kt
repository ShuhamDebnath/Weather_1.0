package com.example.weather10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weather10.data.remote.KtorApiClient
import com.example.weather10.data.repositoryImpl.WeatherRepositoryImpl
import com.example.weather10.domain.repository.WeatherRepository
import com.example.weather10.presentation.screen.WeatherScreen
import com.example.weather10.presentation.viewmodel.WeatherViewModel
import com.example.weather10.ui.theme.Weather10Theme
import io.ktor.client.HttpClient

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val repository: WeatherRepository = WeatherRepositoryImpl(client = KtorApiClient)

            val viewModel = viewModel<WeatherViewModel>(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return WeatherViewModel(repository = repository) as T
                    }
                }
            )

            Weather10Theme {
                WeatherScreen(
                    state = viewModel.weatherState.collectAsState().value,
                    onEvent = viewModel::onWeatherEvent
                )
            }
        }
    }
}
