package com.askolds.homeinventory.featureParameter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParameterSetParameterDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: ParameterEntity): Long

    @Update
    suspend fun update(parameter: ParameterEntity)

    @Query("""
        SELECT * FROM parameter
        ORDER BY LOWER(name), name""")
    fun getList(): Flow<List<ParameterEntity>>

    @Query("SELECT * FROM parameter WHERE id = :id")
    suspend fun getById(id: Int): ParameterEntity?

    @Query("SELECT * FROM parameter WHERE id = :id")
    fun getFlowById(id: Int): Flow<ParameterEntity>

    @Query("""
        SELECT * FROM parameter
        WHERE 
            searchName LIKE '%' || :name || '%'
        ORDER BY LOWER(name), name
        """)
    fun search(name: String): Flow<List<ParameterEntity>>

    @Query("""
        DELETE FROM parameter WHERE id IN (:ids)
    """)
    suspend fun deleteByIds(ids: List<Int>)
}