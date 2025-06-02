package com.amolina.presentation.di

import com.amolina.presentation.ui.viewmodel.CitiesViewModel
import org.koin.dsl.module

val viewModelModule = module {

    factory {
        CitiesViewModel(
            getCitiesUseCase = get(),
            toggleFavouriteUseCase = get(),
            getFavouriteCitiesUseCase = get(),
            getCitiesPagedUseCase = get(),
            searchCitiesUseCase = get(),
        )
    }

}