package com.amolina.presentation.ui.components.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@androidx.compose.runtime.Composable
fun CityMap(
    cities: List<City>,
    modifier: Modifier = Modifier,
    padding: Modifier = Modifier
) {
    if (cities.isNotEmpty()) {
        val cameraPositionState = rememberCameraPositionState()

        LaunchedEffect(cities) {
            if (cities.size == 1) {
                val city = cities.first()
                cameraPositionState.position = CameraPosition(
                    LatLng(city.coord.latitude, city.coord.longitude),
                    12f, 0f, 0f
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
            modifier = modifier.then(padding).testTag("CityMap"),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true
            )
        ) {
            cities.forEach { city ->
                val markerPosition = LatLng(city.coord.latitude, city.coord.longitude)
                com.google.maps.android.compose.Marker(
                    state = remember { MarkerState(position = markerPosition) },
                    title = city.name,
                    snippet = stringResource(R.string.welcome_to_in, city.name, city.countryCode)
                )
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.no_cities_available), style = MaterialTheme.typography.bodyLarge)
        }
    }
}
