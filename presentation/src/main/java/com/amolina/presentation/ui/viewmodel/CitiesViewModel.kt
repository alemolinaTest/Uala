package com.amolina.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amolina.domain.model.City
import com.amolina.domain.usecase.GetCitiesPagedUseCase
import com.amolina.domain.usecase.GetCityByIdUseCase
import com.amolina.domain.usecase.GetFavouriteCitiesUseCase
import com.amolina.domain.usecase.GetRemoteCitiesUseCase
import com.amolina.domain.usecase.SearchCitiesUseCase
import com.amolina.domain.usecase.ToggleFavouriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class CitiesViewModel(
    private val getCitiesPagedUseCase: GetCitiesPagedUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
    private val getCityByIdUseCase: GetCityByIdUseCase,
    getRemoteCitiesUseCase: GetRemoteCitiesUseCase
) : ViewModel(), ICitiesViewModel {

    private val _searchQuery = MutableStateFlow("")
    override val searchQuery: StateFlow<String> get() = _searchQuery

    private val _showFavouritesOnly = MutableStateFlow(false)
    override val showFavouritesOnly: StateFlow<Boolean> get() = _showFavouritesOnly

    private val _selectedCity = MutableStateFlow<City?>(null)
    override val selectedCity: StateFlow<City?> = _selectedCity

    init {
        getRemoteCitiesUseCase
    }

    // This now reacts automatically to changes!
    @OptIn(ExperimentalCoroutinesApi::class)
    override val pagedCities: StateFlow<PagingData<City>> =
        combine(_searchQuery, _showFavouritesOnly) { query, favouritesOnly ->
            query to favouritesOnly
        }
            .flatMapLatest { (query, favouritesOnly) ->
                when {
                    query.isNotBlank() -> searchCitiesUseCase(query, favouritesOnly)
                    favouritesOnly -> getFavouriteCitiesUseCase()
                    else -> getCitiesPagedUseCase()
                }
            }
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    override fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    override fun toggleShowFavourites() {
        _showFavouritesOnly.value = !_showFavouritesOnly.value
    }

    override fun toggleFavourite(cityId: Int) {
        viewModelScope.launch {
            toggleFavouriteUseCase(cityId)
            // No need to manually refresh; Paging will handle it via invalidation
        }
    }

    override fun selectCity(city: Int) {
        viewModelScope.launch {
            val city = getCityByIdUseCase(city)
            _selectedCity.value = city
        }
    }
}