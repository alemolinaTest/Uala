package com.amolina.domain.usecase

import com.amolina.domain.model.City
import com.amolina.domain.repository.CitiesRepository
import com.amolina.domain.util.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCitiesUseCase @Inject constructor(
    private val repository: CitiesRepository
) {
    operator fun invoke(): Flow<Resource<List<City>>> {
        return repository.getCities()
    }
}
