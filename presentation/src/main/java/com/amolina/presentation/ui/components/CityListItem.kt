package com.amolina.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amolina.domain.model.City
import com.amolina.presentation.R

@Composable
fun CityListItem(
    city: City,
    isSelected: Boolean = false,
    onToggleFavourite: () -> Unit,
    onCityClicked: () -> Unit,
    onInfoClicked: () -> Unit,
) {
    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.tertiary
    else
        MaterialTheme.colorScheme.secondary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onCityClicked() }
            .testTag("CityCard"),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${city.name}, ${city.countryCode}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onInfoClicked) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.city_info),
                        )
                    }
                    IconButton(onClick = onToggleFavourite) {
                        Icon(
                            imageVector = if (city.isFavourite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = if (city.isFavourite) stringResource(R.string.unfavourite) else stringResource(
                                R.string.favourite
                            )
                        )
                    }
                }
            }
        }
    }
}
