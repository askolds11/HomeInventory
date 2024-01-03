package com.askolds.homeinventory.featureParameter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParameterSetDao {
    // region Single
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: ParameterSetEntity): Long

    @Update
    suspend fun update(parameterSet: ParameterSetEntity)

    @Query("SELECT * FROM parameter_set WHERE id = :id")
    suspend fun getById(id: Int): ParameterSetEntity?

    @Query("SELECT * FROM parameter_set WHERE id = :id")
    fun getFlowById(id: Int): Flow<ParameterSetEntity>

    // endregion Single
    // region List
    @Query(
        """
        SELECT * FROM parameter_set
        ORDER BY LOWER(name), name
        """
    )
    fun getFlowList(): Flow<List<ParameterSetEntity>>

    @Query(
        """
        SELECT * FROM parameter_set
        WHERE 
            searchName LIKE '%' || :name || '%'
        ORDER BY LOWER(name), name
        """
    )
    fun getFlowListByName(name: String): Flow<List<ParameterSetEntity>>

    @Query(
        """
        DELETE FROM parameter_set WHERE id IN (:ids)
        """
    )
    suspend fun deleteByIds(ids: List<Int>)

    // endregion List
    // region Other
    @Query(
        """
        SELECT (
            CASE
                WHEN EXISTS (
                    SELECT 1
                    FROM parameter_set
                    WHERE name = :name
                    AND (:excludeName IS NULL OR name != :excludeName)
                ) THEN 1
                ELSE 0
            END)
        """
    )
    suspend fun nameExists(name: String, excludeName: String?): Boolean
    // endregion Other
}