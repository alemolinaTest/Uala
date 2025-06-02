package com.amolina.domain.usecase

import com.amolina.domain.repository.CitiesRepository

class ToggleFavouriteUseCase(private val repository: CitiesRepository) {
    suspend operator fun invoke(cityId: Int) {
        repository.toggleFavourite(cityId)
    }
}
