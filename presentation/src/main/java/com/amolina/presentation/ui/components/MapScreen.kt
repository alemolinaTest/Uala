package com.amolina.presentation.ui.components

import android.Manifest
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.amolina.domain.model.City
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@RequiresApi(35)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    city: City,
    onBackPressed: () -> Unit
) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        // Show the map
        GoogleMapScreenScaffold(cities = listOf(city), onBackPressed)
    } else {
        // Request permissions
        LocationPermissionScreen(locationPermissionsState)
    }
}
