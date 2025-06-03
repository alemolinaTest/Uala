package com.amolina.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amolina.domain.model.City
import com.amolina.domain.usecase.*
import com.amolina.domain.util.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class CitiesViewModel(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    private val getCitiesPagedUseCase: GetCitiesPagedUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase
) : ViewModel() , ICitiesViewModel{

    private val _citiesState = MutableStateFlow<Resource<List<City>>>(Resource.Loading)
    override val citiesState: StateFlow<Resource<List<City>>> get() = _citiesState

    override val pagedCities: Flow<PagingData<City>> =
        getCitiesPagedUseCase().cachedIn(viewModelScope)

    private val _searchQuery = MutableStateFlow("")
    override val searchQuery: StateFlow<String> get() = _searchQuery

    private val _searchResults = MutableStateFlow<List<City>>(emptyList())
    override val searchResults: StateFlow<List<City>> get() = _searchResults

    private val _showFavouritesOnly = MutableStateFlow(false)
    override val showFavouritesOnly: StateFlow<Boolean> get() = _showFavouritesOnly

    private val _selectedCity = MutableStateFlow<City?>(null)
    override val selectedCity: StateFlow<City?> = _selectedCity


    init {
        // Initial fetch
        fetchCities()
        observeCitiesState()
    }

    private fun observeCitiesState() {
        viewModelScope.launch {
            _citiesState.collectLatest { resource ->
                if (_searchQuery.value.isBlank() && resource is Resource.Success) {
                    _searchResults.value = resource.data
                }
            }
        }
    }

    override fun fetchCities() {
        viewModelScope.launch {
            val flow = if (showFavouritesOnly.value) {
                getFavouriteCitiesUseCase()
            } else {
                getCitiesUseCase()
            }
            flow.collectLatest { result ->
                _citiesState.value = result
            }
        }
    }

    override fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        performSearch()
    }

    private fun performSearch() {
        viewModelScope.launch {
            if (_searchQuery.value.isBlank()) {
                // If the search query is empty, just show all cities from latest state
                val current = _citiesState.value
                if (current is Resource.Success) {
                    _searchResults.value = current.data
                }
            } else {
                val filteredCities = searchCitiesUseCase(
                    prefix = _searchQuery.value,
                    favouritesOnly = showFavouritesOnly.value
                )
                _searchResults.value = filteredCities
            }
        }
    }

    fun getCityById(cityId: Int): City? {
        // 1. Try search results first (if search is active)
        val searchCities = _searchResults.value
        val fromSearch = searchCities.find { it.id == cityId }
        if (fromSearch != null) return fromSearch

        // 2. Otherwise, get from all cities
        val allCities = (_citiesState.value as? Resource.Success)?.data ?: emptyList()
        return allCities.find { it.id == cityId }
    }

    override fun toggleShowFavourites() {
        _showFavouritesOnly.value = !_showFavouritesOnly.value
        fetchCities()
        performSearch()
    }

    override fun toggleFavourite(cityId: Int) {
        viewModelScope.launch {
            toggleFavouriteUseCase(cityId)
            fetchCities()
            performSearch()
        }
    }

    override fun selectCity(cityId: Int) {
        _selectedCity.value = getCityById(cityId)
    }
}