package com.amolina.data.di

import com.amolina.data.remote.CitiesApiService
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { OkHttpClient.Builder().build() }
    single { Json { ignoreUnknownKeys = true } }
    single { provideCitiesApiService(get(), get()) }
}

fun provideCitiesApiService(
    client: OkHttpClient,
    json: Json
): CitiesApiService {
    val url = "https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json"
    return CitiesApiService(client, json, url)
}
