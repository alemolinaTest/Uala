package com.amolina.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amolina.domain.model.City
import com.amolina.domain.usecase.GetCitiesUseCase
import com.amolina.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CitiesViewModel(
    private val getCitiesUseCase: GetCitiesUseCase
) : ViewModel() {

    private val _citiesState = MutableStateFlow<Resource<List<City>>>(Resource.Loading)
    val citiesState: StateFlow<Resource<List<City>>> get() = _citiesState

    init {
        fetchCities()
    }

    private fun fetchCities() {
        viewModelScope.launch {
            getCitiesUseCase().collectLatest { result ->
                _citiesState.value = result
            }
        }
    }
}
