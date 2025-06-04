package com.amolina.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CitiesDao {
    @Query("SELECT * FROM cities ORDER BY name ASC")
    fun getAllCitiesPaged(): PagingSource<Int, CityEntity>

    @Query("SELECT * FROM cities WHERE isFavourite = 1 ORDER BY name ASC")
    fun getFavouriteCitiesPaged(): PagingSource<Int, CityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<CityEntity>)

    @Query("SELECT * FROM cities WHERE id = :cityId LIMIT 1")
    suspend fun getCityById(cityId: Int): CityEntity?

    @Query("UPDATE cities SET isFavourite = :isFavourite WHERE id = :cityId")
    suspend fun updateFavourite(cityId: Int, isFavourite: Boolean)

    @Query("""
        SELECT * FROM cities
        WHERE name LIKE :prefix || '%' COLLATE NOCASE
        ORDER BY name ASC, countryCode ASC
    """)
    fun searchCitiesPaged(prefix: String): PagingSource<Int, CityEntity>

    @Query("""
        SELECT * FROM cities
        WHERE name LIKE :prefix || '%' COLLATE NOCASE AND isFavourite = 1
        ORDER BY name ASC, countryCode ASC
    """)
    fun searchFavouriteCitiesPaged(prefix: String): PagingSource<Int, CityEntity>

    @Query("SELECT id FROM cities WHERE isFavourite = 1")
    suspend fun getFavouriteCityIds(): List<Int>

}

