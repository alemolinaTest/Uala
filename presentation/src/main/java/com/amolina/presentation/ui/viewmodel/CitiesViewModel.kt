package com.amolina.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amolina.domain.model.City
import com.amolina.domain.usecase.GetCitiesUseCase
import com.amolina.domain.usecase.GetFavouriteCitiesUseCase
import com.amolina.domain.usecase.ToggleFavouriteUseCase
import com.amolina.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CitiesViewModel(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase
) : ViewModel() {

    private val _citiesState = MutableStateFlow<Resource<List<City>>>(Resource.Loading)
    val citiesState: StateFlow<Resource<List<City>>> get() = _citiesState

    private var showFavouritesOnly = false
    fun isShowingFavourites(): Boolean = showFavouritesOnly

    init {
        fetchCities()
    }

    fun fetchCities() {
        viewModelScope.launch {
            val flow = if (showFavouritesOnly) {
                getFavouriteCitiesUseCase()
            } else {
                getCitiesUseCase()
            }
            flow.collectLatest { result ->
                _citiesState.value = result
            }
        }
    }

    fun toggleShowFavourites() {
        showFavouritesOnly = !showFavouritesOnly
        fetchCities()
    }

    fun toggleFavourite(cityId: Int) {
        viewModelScope.launch {
            toggleFavouriteUseCase(cityId)
            fetchCities() // refresh data after toggling
        }
    }
}
