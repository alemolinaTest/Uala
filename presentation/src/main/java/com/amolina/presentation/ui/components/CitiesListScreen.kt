package com.amolina.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amolina.domain.model.City
import com.amolina.domain.util.Resource
import com.amolina.presentation.ui.viewmodel.CitiesViewModel

@Composable
fun CitiesListScreen(
    viewModel: CitiesViewModel,
    onCityClicked: (Int) -> Unit,
    usePaging: Boolean = false // default to non-paged mode
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Filter toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show Favourites Only")
            Switch(
                checked = viewModel.isShowingFavourites(),
                onCheckedChange = { viewModel.toggleShowFavourites() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (usePaging) {
            // Paged data
            val pagedCities = viewModel.pagedCities.collectAsLazyPagingItems()

            LazyColumn {
                items(pagedCities.itemCount) { index ->
                    val city = pagedCities[index]
                    if (city != null) {
                        CityListItem(
                            city = city,
                            onToggleFavourite = { viewModel.toggleFavourite(city.id) },
                            onCityClicked = { onCityClicked(city.id) }
                        )
                    }
                }

                pagedCities.apply {
                    when {
                        loadState.refresh is androidx.paging.LoadState.Loading -> {
                            item { LoaderItemComposable() }
                        }
                        loadState.append is androidx.paging.LoadState.Loading -> {
                            item { LoaderItemComposable() }
                        }
                        loadState.refresh is androidx.paging.LoadState.Error -> {
                            val e = loadState.refresh as androidx.paging.LoadState.Error
                            item { ErrorItemComposable(e.error.message ?: "Unknown error") }
                        }
                        loadState.append is androidx.paging.LoadState.Error -> {
                            val e = loadState.append as androidx.paging.LoadState.Error
                            item { ErrorItemComposable(e.error.message ?: "Unknown error") }
                        }
                    }
                }
            }
        } else {
            // Non-paged data
            val citiesState by viewModel.citiesState.collectAsState()

            when (citiesState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Resource.Success -> {
                    val cities = (citiesState as Resource.Success<List<City>>).data
                    LazyColumn {
                        items(cities) { city ->
                            CityListItem(
                                city = city,
                                onToggleFavourite = { viewModel.toggleFavourite(city.id) },
                                onCityClicked = { onCityClicked(city.id) }
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${(citiesState as Resource.Error).message ?: "Unknown error"}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun LoaderItemComposable() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorItemComposable(message: String) {
    Text(
        text = "Error: $message",
        color = MaterialTheme.colorScheme.error
    )
}