package com.amolina.data.di

import com.amolina.domain.usecase.GetCitiesUseCase
import org.koin.dsl.module

val useCaseModule = module {
   single{
       GetCitiesUseCase(repository = get())
   }
}