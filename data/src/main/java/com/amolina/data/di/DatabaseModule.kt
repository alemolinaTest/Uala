package com.amolina.data.di

import androidx.room.Room
import com.amolina.data.local.AppDatabase
import com.amolina.data.repository.CitiesRepositoryImpl
import com.amolina.domain.repository.CitiesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_db"
        ).build()
    }

    single { get<AppDatabase>().citiesDao() }
    single<CoroutineDispatcher> { Dispatchers.IO }


    single<CitiesRepository> {
        CitiesRepositoryImpl(
            dao = get(),
            api = get(),)
    }
}
