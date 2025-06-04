package com.amolina.domain.usecase

import androidx.paging.PagingData
import com.amolina.domain.model.City
import com.amolina.domain.repository.CitiesRepository
import kotlinx.coroutines.flow.Flow

class SearchCitiesUseCase(private val repository: CitiesRepository) {
     operator fun invoke(prefix: String, favouritesOnly: Boolean): Flow<PagingData<City>> {
        return repository.searchCitiesPaged(prefix, favouritesOnly)
    }
}
