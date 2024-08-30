package com.example.weatherapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.R
import com.example.weatherapp.api.NetWorkResponse
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.ui.theme.ColorBackground
import com.example.weatherapp.ui.theme.ColorTextPrimary
import com.example.weatherapp.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    var city by remember {
        mutableStateOf("")
    }

    val weatherResult = viewModel.weatherResult.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ColorBackground,
    ) { paddings ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddings)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = city,
                    onValueChange = {
                        city = it
                    },
                    label = {
                        Text(text = "Search for any location")
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black
                    )
                )
                IconButton(onClick = {
                    viewModel.getWeatherByCity(city)
                    keyboardController?.hide()
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search for any location"
                    )
                }

            }
            when (val result = weatherResult.value) {
                is NetWorkResponse.Error -> {
                    Text(text = result.message)
                }

                NetWorkResponse.Loading -> {
                    CircularProgressIndicator()
                }

                is NetWorkResponse.Success -> {
                    WeatherDetail(data = result.data)
                }

                null -> {}
            }

        }
    }
}

@Composable
fun WeatherDetail(data: WeatherModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_location_pin),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.height(18.dp)
            )
            Text(
                text = data.city.name,
                style = MaterialTheme.typography.titleLarge,
                color = ColorTextPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${data.list[0].main.temp} °C ",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

            AsyncImage(
                modifier = Modifier
                    .size(160.dp)
                    .shadow(elevation = 1.dp),
                model = "https://openweathermap.org/img/wn/${data.list[0].weather[0].icon}d@2x.png",
                contentDescription = null,
            )


        Text(
            text = data.list[0].weather[0].main,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherInfo(key = "Humidity", value = data.list[0].main.humidity)
                    WeatherInfo(key = "Wind Speed", value = data.list[0].wind.speed)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherInfo(key = "Clouds", value = data.list[0].clouds.all)
                    WeatherInfo(key = "Sea Level", value = data.list[0].main.sea_level)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherInfo(key = "Feels Like", value = data.list[0].main.feels_like)
                    WeatherInfo(key = "Visibility", value = data.list[0].visibility)
                }
            }
        }
    }
}

@Composable
private fun WeatherInfo(
    key: String,
    value: String
){
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}
