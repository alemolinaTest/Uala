package com.amolina.domain.usecase

import com.amolina.domain.model.City
import com.amolina.domain.repository.CitiesRepository

class GetCityByIdUseCase(private val repository: CitiesRepository) {
    suspend operator fun invoke(cityId: Int): City? = repository.getCityById(cityId)
}
