package com.example.weather10.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather10.R
import com.example.weather10.data.remote.model.WeatherResponse
import com.example.weather10.presentation.event.WeatherEvent
import com.example.weather10.presentation.state.WeatherState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    state: WeatherState,
    onEvent: (WeatherEvent) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Weather App")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )

            )
        }
    ) { padding ->


        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.search,
                    onValueChange = {
                        onEvent(WeatherEvent.OnSearchUpdate(it))
                    },
                    modifier = Modifier.weight(1f),
                    keyboardActions = KeyboardActions(onSearch = {
                        onEvent(WeatherEvent.OnSearchClicked)
                        focusManager.clearFocus()
                    }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    label = {
                        Text("city")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer
                    )

                )
                Spacer(Modifier.size(8.dp))
                Icon(
                    Icons.Default.Search,
                    contentDescription = "search",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            onEvent(WeatherEvent.OnSearchClicked)
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                )

            }
            Spacer(Modifier.size(12.dp))


            if (state.loading) {
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
                    CircularProgressIndicator()
                }
            }
            if (state.error.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onEvent(WeatherEvent.OnSearchClicked)
                        }) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                    )
                    Text(
                        "Something ent Wrong. refresh!!",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            if (state.error.isEmpty() && !state.loading && state.weatherResponse == null) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onEvent(WeatherEvent.OnSearchClicked)
                        }) {

                    Image(
                        painter = painterResource(R.drawable.weather_logo),
                        contentDescription = "logo",
                        modifier = Modifier
                            .size(64.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                    )
                    Spacer(Modifier.size(24.dp))
                    Text(
                        "Have a nice day, Dude. Know about your city",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            state.weatherResponse?.let {
                WeatherInfo(it, onEvent)
            }
        }
    }

}

@Composable
fun WeatherInfo(weatherResponse: WeatherResponse, onEvent: (WeatherEvent) -> Unit) {
    var tempInCelsius by remember {
        mutableStateOf(true)
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(.2f))
        Row(
            modifier = Modifier.clickable {
                onEvent(WeatherEvent.OnRefreshClicked)
            },
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = "last updated on",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = weatherResponse.current.lastUpdated,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.size(8.dp))
            Icon(
                Icons.Filled.Refresh,
                contentDescription = "refresh"
            )
        }

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.LocationOn,
                contentDescription = "location",
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = weatherResponse.location.name,
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = weatherResponse.location.country,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
        }
        Spacer(Modifier.weight(.3f))


        Spacer(Modifier.weight(1f))

        Text(
            text = if (tempInCelsius) "${weatherResponse.current.tempC} °C" else "${weatherResponse.current.tempF} °F",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .clickable {
                    tempInCelsius = !tempInCelsius
                },
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(1f))

        AsyncImage(
            modifier = Modifier.size(128.dp),
            model = "https:${weatherResponse.current.condition.icon}".replace(
                "64x64",
                "128x128"
            ), contentDescription = "icon"
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = weatherResponse.current.condition.text,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.weight(1f))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyValue("Humidity", weatherResponse.current.humidity.toString())
                    WeatherKeyValue(
                        "Wind Speed",
                        weatherResponse.current.windKph.toString() + " Km/h"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyValue("UV", weatherResponse.current.uv.toString())
                    WeatherKeyValue(
                        "Participation",
                        weatherResponse.current.precipMm.toString() + " mm"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherKeyValue("Local time", weatherResponse.location.localtime.split(" ")[1])
                    WeatherKeyValue("Local data", weatherResponse.location.localtime.split(" ")[0])
                }
            }
        }

    }

}

@Composable
fun WeatherKeyValue(key: String, value: String) {
    Column(
        Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold)

    }

}

@Preview(showSystemUi = true)
@Composable
private fun WeatherScreenPrev() {
    WeatherScreen(state = WeatherState(), onEvent = {})
}