package com.amolina.presentation.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CityDetailScreen(cityId: Int) {
    // Here you can fetch details from DB or API
    Text(text = "Details for City ID: $cityId")
}
