package com.amolina.presentation.ui.components.maps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amolina.presentation.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(locationPermissionsState: MultiplePermissionsState) {
    // Use Accompanist to remember the state of multiple permissions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        when {
            locationPermissionsState.allPermissionsGranted -> {
                Text(stringResource(R.string.location_permissions_granted_you_can_use_location_based_features))
            }
            else -> {
                // Initial state or denied without rationale
                LaunchedEffect(Unit) {
                    locationPermissionsState.launchMultiplePermissionRequest()
                }
            }
        }
    }
}