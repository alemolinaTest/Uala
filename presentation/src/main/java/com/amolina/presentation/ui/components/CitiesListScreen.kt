package com.amolina.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.amolina.domain.model.City
import com.amolina.domain.util.Resource
import com.amolina.presentation.ui.viewmodel.CitiesViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesListScreen(
    viewModel: CitiesViewModel,
    onCityClicked: (Int) -> Unit,
    usePaging: Boolean = false
) {
    val isFavourites by viewModel.showFavouritesOnly.collectAsState()
    val selectedCityId by viewModel.selectedCity.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cities App") },
                navigationIcon = {
                    Icon(imageVector = Icons.Default.LocationCity, contentDescription = null)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            SearchField(
                query = viewModel.searchQuery.collectAsState().value,
                onQueryChanged = { viewModel.updateSearchQuery(it) },
                onClearQuery = {
                    viewModel.updateSearchQuery("")
                    viewModel.fetchCities()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FilterRow(
                isFavourites = isFavourites,
                onToggleFavourites = { viewModel.toggleShowFavourites() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (usePaging) {
                PagedCitiesList(
                    pagedCities = viewModel.pagedCities,
                    selectedCityId = selectedCityId?.id,
                    onToggleFavourite = { viewModel.toggleFavourite(it) },
                    onCityClicked = onCityClicked
                )
            } else {
                NonPagedCitiesList(
                    citiesState = viewModel.citiesState,
                    searchResults = viewModel.searchResults,
                    selectedCityId = selectedCityId?.id,
                    onToggleFavourite = { viewModel.toggleFavourite(it) },
                    onCityClicked = onCityClicked
                )
            }
        }
    }
}

@Composable
internal fun NonPagedCitiesList(
    citiesState: StateFlow<Resource<List<City>>>,
    searchResults: StateFlow<List<City>>,
    selectedCityId: Int?,
    onToggleFavourite: (Int) -> Unit,
    onCityClicked: (Int) -> Unit
) {
    val state by citiesState.collectAsState()
    val results by searchResults.collectAsState()

    when (state) {
        is Resource.Loading -> LoaderItem()
        is Resource.Success -> {
            LazyColumn {
                items(results) { city ->
                    CityListItem(
                        city = city,
                        onToggleFavourite = { onToggleFavourite(city.id) },
                        onCityClicked = { onCityClicked(city.id) },
                        isSelected = city.id == selectedCityId
                    )
                }
            }
        }
        is Resource.Error -> {
            val error = (state as Resource.Error).message ?: "Unknown error"
            ErrorItem(error)
        }
    }
}

@Composable
internal fun PagedCitiesList(
    pagedCities: Flow<PagingData<City>>,
    selectedCityId: Int?,
    onToggleFavourite: (Int) -> Unit,
    onCityClicked: (Int) -> Unit
) {
    val items = pagedCities.collectAsLazyPagingItems()

    LazyColumn {
        items(items.itemCount) { index ->
            val city = items[index]
            city?.let {
                CityListItem(
                    city = it,
                    onToggleFavourite = { onToggleFavourite(it.id) },
                    onCityClicked = { onCityClicked(it.id) },
                    isSelected = it.id == selectedCityId
                )
            }
        }

        items.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoaderItem() }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoaderItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = loadState.refresh as LoadState.Error
                    item { ErrorItem(e.error.message ?: "Unknown error") }
                }
                loadState.append is LoadState.Error -> {
                    val e = loadState.append as LoadState.Error
                    item { ErrorItem(e.error.message ?: "Unknown error") }
                }
            }
        }
    }
}

@Composable
internal fun LoaderItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun ErrorItem(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: $message",
            color = MaterialTheme.colorScheme.error
        )
    }
}
