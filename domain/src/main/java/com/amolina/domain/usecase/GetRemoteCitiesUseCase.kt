package com.amolina.domain.usecase

import com.amolina.domain.repository.CitiesRepository

class GetRemoteCitiesUseCase(private val repository: CitiesRepository) {
    suspend operator fun invoke(): Unit = repository.refreshCities()
}
