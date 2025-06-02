package com.amolina.data.di

import com.amolina.data.BuildConfig
import com.amolina.data.remote.CitiesApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { OkHttpClient.Builder().build() }
    single { Json { ignoreUnknownKeys = true } }
    single { provideCitiesApiService(get(), get(), get()) }
}

fun provideCitiesApiService(
    client: OkHttpClient,
    json: Json,
    ioDispatcher: CoroutineDispatcher
): CitiesApiService {
    val url = BuildConfig.CITIES_URL
    return CitiesApiService(client, json, url, ioDispatcher)
}
