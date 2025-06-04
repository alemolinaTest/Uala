package com.amolina.presentation.ui.components.maps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.amolina.presentation.ui.components.FilterRow
import com.amolina.presentation.ui.components.PagedCitiesList
import com.amolina.presentation.ui.components.SearchField
import com.amolina.presentation.ui.viewmodel.ICitiesViewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun LandscapeCitiesMapScreen(
    viewModel: ICitiesViewModel,
    onInfoClicked: (Int) -> Unit,
) {
    val isFavourites by viewModel.showFavouritesOnly.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Row(modifier = Modifier.fillMaxSize()) {
        // Left side: City List
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            // Search Field with Clear Button
            SearchField(
                query = searchQuery,
                onQueryChanged = { viewModel.updateSearchQuery(it) },
                onClearQuery = { viewModel.updateSearchQuery("") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FilterRow(
                isFavourites = isFavourites,
                onToggleFavourites = { viewModel.toggleShowFavourites() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            PagedCitiesList(
                pagedCities = viewModel.pagedCities,
                selectedCityId = selectedCity?.id,
                onToggleFavourite = { viewModel.toggleFavourite(it) },
                onCityClicked = { viewModel.selectCity(it) },
                onInfoClicked = { onInfoClicked(it) }
            )
        }

        // Right side: Map View
        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight()
        ) {
            selectedCity?.let { city ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("GoogleMapContainer"),
                    contentAlignment = Alignment.Center
                ) {
                    GoogleMapScreen(cities = listOf(city))
                }
            } ?: Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text("Select a city from the list", style = MaterialTheme.typography.bodyLarge)

            }
        }
    }
}
