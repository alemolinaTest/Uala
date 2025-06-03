package com.amolina.presentation.ui.components.maps

import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.amolina.domain.model.City
import com.amolina.presentation.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(35)
@Composable
fun GoogleMapScreen(
    cities: List<City>,
) {

    if (cities.isNotEmpty()) {
        val cameraPositionState = rememberCameraPositionState()
        LaunchedEffect(cities) {
            if (cities.size == 1) {
                val city = cities.first()
                cameraPositionState.position = CameraPosition(
                    LatLng(city.coord.latitude, city.coord.longitude),
                    12f,
                    0f,
                    0f
                )
            } else {
                val boundsBuilder = LatLngBounds.Builder()
                cities.forEach { city ->
                    boundsBuilder.include(LatLng(city.coord.latitude, city.coord.longitude))
                }
                val bounds = boundsBuilder.build()
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                cameraPositionState.move(cameraUpdate)
            }
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxSize().testTag("GoogleMap"),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true,
                mapType = MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true
            )
        ) {
            cities.forEach { city ->
                val markerPosition = LatLng(city.coord.latitude, city.coord.longitude)
                Marker(
                    state = MarkerState(position = markerPosition),
                    title = city.name,
                    snippet = stringResource(R.string.welcome_to_in, city.name, city.countryCode)
                )
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.no_cities_available),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
