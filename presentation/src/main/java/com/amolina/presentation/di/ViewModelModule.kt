package com.amolina.presentation.di

import com.amolina.presentation.ui.viewmodel.CitiesViewModel
import org.koin.dsl.module

val viewModelModule = module {

    factory {
        CitiesViewModel(
            toggleFavouriteUseCase = get(),
            getFavouriteCitiesUseCase = get(),
            getCitiesPagedUseCase = get(),
            searchCitiesUseCase = get(),
            getCityByIdUseCase = get(),
            getRemoteCitiesUseCase = get()
        )
    }

}