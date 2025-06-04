package com.amolina.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.amolina.domain.model.City
import com.amolina.presentation.R
import com.amolina.presentation.ui.viewmodel.ICitiesViewModel
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesListScreen(
    viewModel: ICitiesViewModel,
    onCityClicked: (Int) -> Unit,
    onInfoClicked: (Int) -> Unit,
) {
    val isFavourites by viewModel.showFavouritesOnly.collectAsState()
    val selectedCityId by viewModel.selectedCity.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.cities_app),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationCity,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
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
                query = searchQuery,
                onQueryChanged = { viewModel.updateSearchQuery(it) },
                onClearQuery = { viewModel.updateSearchQuery("") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FilterRow(
                isFavourites = isFavourites,
                onToggleFavourites = { viewModel.toggleShowFavourites() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Always use PagedCitiesList now
            PagedCitiesList(
                pagedCities = viewModel.pagedCities,
                selectedCityId = selectedCityId?.id,
                onToggleFavourite = { viewModel.toggleFavourite(it) },
                onCityClicked = onCityClicked,
                onInfoClicked = onInfoClicked
            )
        }
    }
}

@Composable
internal fun PagedCitiesList(
    pagedCities: Flow<PagingData<City>>,
    selectedCityId: Int?,
    onToggleFavourite: (Int) -> Unit,
    onCityClicked: (Int) -> Unit,
    onInfoClicked: (Int) -> Unit,
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
                    isSelected = it.id == selectedCityId,
                    onInfoClicked = { onInfoClicked(it.id) }
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
        CircularProgressIndicator(
            modifier = Modifier.testTag("CircularProgressIndicator")
        )
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
