package com.amolina.data.remote

import com.amolina.domain.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

class CitiesApiService (
    private val client: OkHttpClient,
    private val json: Json,
    private val citiesUrl: String
) {
    suspend fun fetchCities(): List<City> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(citiesUrl).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Failed to fetch cities: ${response.code}")
            }
            val jsonString = response.body?.string() ?: throw Exception("Empty response body")
            return@use json.decodeFromString<List<City>>(jsonString)
        }
    }
}