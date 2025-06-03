package com.amolina.presentation.ui.viewmodel

import androidx.paging.PagingData
import com.amolina.domain.model.City
import com.amolina.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ICitiesViewModel {
    val showFavouritesOnly: StateFlow<Boolean>
    val selectedCity: StateFlow<City?>
    val citiesState: StateFlow<Resource<List<City>>>
    val searchResults: StateFlow<List<City>>
    val searchQuery: StateFlow<String>
    val pagedCities: Flow<PagingData<City>>

    fun updateSearchQuery(query: String)
    fun fetchCities()
    fun toggleShowFavourites()
    fun toggleFavourite(cityId: Int)
    fun selectCity(cityId: Int)
}