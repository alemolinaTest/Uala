package com.amolina.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CitiesDao {
    @Query("SELECT * FROM cities")
    suspend fun getAllCities(): List<CityEntity>

    @Query("SELECT * FROM cities WHERE isFavourite = 1")
    suspend fun getFavouriteCities(): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<CityEntity>)

    @Query("SELECT * FROM cities WHERE id = :cityId LIMIT 1")
    suspend fun getCityById(cityId: Int): CityEntity?

    @Query("UPDATE cities SET isFavourite = :isFavourite WHERE id = :cityId")
    suspend fun updateFavourite(cityId: Int, isFavourite: Boolean)

    @Query("SELECT * FROM cities ORDER BY name ASC")
    fun getAllCitiesPaged(): PagingSource<Int, CityEntity>

}