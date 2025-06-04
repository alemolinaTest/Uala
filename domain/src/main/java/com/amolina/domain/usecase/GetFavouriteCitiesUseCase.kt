package com.amolina.domain.usecase

import androidx.paging.PagingData
import com.amolina.domain.model.City
import com.amolina.domain.repository.CitiesRepository
import kotlinx.coroutines.flow.Flow

class GetFavouriteCitiesUseCase(private val repository: CitiesRepository) {
    operator fun invoke(): Flow<PagingData<City>> = repository.getFavouriteCitiesPaged()
}
