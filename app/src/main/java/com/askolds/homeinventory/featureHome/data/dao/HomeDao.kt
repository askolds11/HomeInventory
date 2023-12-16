package com.askolds.homeinventory.featureHome.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: HomeEntity): Long
    @Update
    suspend fun update(item: HomeEntity)
    @Query("SELECT * FROM home ORDER BY LOWER(name), name")
    fun getList(): Flow<List<HomeEntity>>

    @Query("SELECT * FROM home WHERE id = :id")
    fun getById(id: Int): HomeEntity?

    @Query("SELECT * FROM home WHERE id = :id")
    fun getFlowById(id: Int): Flow<HomeEntity>

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
        WHERE searchName LIKE '%' || :name || '%'
        ORDER BY LOWER(name), name
        """)
    fun search(name: String): Flow<List<HomeEntity>>

    @Query("""
        DELETE FROM home WHERE id IN (:ids)
    """)
    suspend fun deleteByIds(ids: List<Int>)

    @Query("""
        SELECT imageId FROM home WHERE id IN (:ids)
    """)
    suspend fun getImageIdsByIds(ids: List<Int>): List<Int>
}