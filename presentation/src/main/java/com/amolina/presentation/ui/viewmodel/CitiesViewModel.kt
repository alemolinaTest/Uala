package com.amolina.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amolina.domain.model.City
import com.amolina.domain.usecase.GetCitiesPagedUseCase
import com.amolina.domain.usecase.GetCitiesUseCase
import com.amolina.domain.usecase.GetFavouriteCitiesUseCase
import com.amolina.domain.usecase.ToggleFavouriteUseCase
import com.amolina.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// at first i wanted Data-layer class injected directly
// because android pager library cant be used on domain module
// to create a use case
//but i decide to get android paging functionality on domain module
// by creating a use case. in order to use data-layer class and have a better separation of concerns
// to test
class CitiesViewModel(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    getCitiesPagedUseCase: GetCitiesPagedUseCase,

) : ViewModel() {

    private val _citiesState = MutableStateFlow<Resource<List<City>>>(Resource.Loading)
    val citiesState: StateFlow<Resource<List<City>>> get() = _citiesState

    // Paged data
    val pagedCities: Flow<PagingData<City>> =
        getCitiesPagedUseCase().cachedIn(viewModelScope)

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
