package com.amolina.domain.usecase

import com.amolina.domain.model.City
import com.amolina.domain.repository.CitiesRepository

class SearchCitiesUseCase(private val repository: CitiesRepository) {
    suspend operator fun invoke(prefix: String, favouritesOnly: Boolean): List<City> {
        return repository.searchCities(prefix, favouritesOnly)
    }
}
