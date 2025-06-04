package com.amolina.data.di

import com.amolina.domain.usecase.GetCitiesPagedUseCase
import com.amolina.domain.usecase.GetCityByIdUseCase
import com.amolina.domain.usecase.GetFavouriteCitiesUseCase
import com.amolina.domain.usecase.GetRemoteCitiesUseCase
import com.amolina.domain.usecase.SearchCitiesUseCase
import com.amolina.domain.usecase.ToggleFavouriteUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetCitiesPagedUseCase(repository = get()) }
    single { GetFavouriteCitiesUseCase(repository = get()) }
    single { ToggleFavouriteUseCase(repository = get()) }
    single { SearchCitiesUseCase(repository = get()) }
    single { GetCityByIdUseCase(repository = get()) }
    single { GetRemoteCitiesUseCase(repository = get()) }
}