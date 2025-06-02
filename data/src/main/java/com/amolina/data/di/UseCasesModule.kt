package com.amolina.data.di

import com.amolina.domain.usecase.GetCitiesPagedUseCase
import com.amolina.domain.usecase.GetCitiesUseCase
import com.amolina.domain.usecase.GetFavouriteCitiesUseCase
import com.amolina.domain.usecase.ToggleFavouriteUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single{ GetCitiesUseCase(repository = get()) }
    single{ GetFavouriteCitiesUseCase(repository = get()) }
    single{ ToggleFavouriteUseCase(repository = get()) }
    factory { GetCitiesPagedUseCase(get()) }
}