package com.amolina.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amolina.domain.model.City

@Composable
fun CityListItem(
    city: City,
    onToggleFavourite: () -> Unit,
    onCityClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onCityClicked() },
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${city.name}, ${city.countryCode}", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onToggleFavourite) {
                    Icon(
                        imageVector = if (city.isFavourite) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = if (city.isFavourite) "Unfavourite" else "Favourite"
                    )
                }
            }
        }
    }
}
