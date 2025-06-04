package com.amolina.presentation.ui.viewmodel

import androidx.paging.PagingData
import com.amolina.domain.model.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ICitiesViewModel {
    val showFavouritesOnly: StateFlow<Boolean>
    val selectedCity: StateFlow<City?>
    val searchQuery: StateFlow<String>
    val pagedCities: Flow<PagingData<City>>

    fun updateSearchQuery(query: String)
    fun toggleShowFavourites()
    fun toggleFavourite(cityId: Int)
    fun selectCity(city: Int)
}
