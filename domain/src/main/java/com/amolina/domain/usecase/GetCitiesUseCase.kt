package com.amolina.domain.usecase

import com.amolina.domain.model.City
import com.amolina.domain.repository.CitiesRepository
import com.amolina.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class GetCitiesUseCase (private val repository: CitiesRepository) {
    operator fun invoke(): Flow<Resource<List<City>>> = repository.getCities()
}
