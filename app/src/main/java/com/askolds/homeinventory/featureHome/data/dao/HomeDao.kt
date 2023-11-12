package com.askolds.homeinventory.featureHome.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: HomeEntity)

    @Query("SELECT * FROM home ORDER BY LOWER(name), name")
    fun getList(): Flow<List<HomeEntity>>

    @Query("SELECT * FROM home WHERE id = :id")
    suspend fun getById(id: Int): HomeEntity?

    @Query("""
        SELECT (
            CASE
                WHEN EXISTS (
                    SELECT 1
                    FROM home
                    WHERE name = :name
                    AND (:excludeName IS NULL OR name != :excludeName)
                ) THEN 1
                ELSE 0
            END)""")
    suspend fun nameExists(name: String, excludeName: String?): Boolean

    @Query("""
        SELECT * FROM home
        WHERE searchName LIKE '%' || LOWER(:name) || '%'
        ORDER BY LOWER(name), name
        """)
    fun search(name: String): Flow<List<HomeEntity>>
}