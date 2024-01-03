package com.askolds.homeinventory.featureParameter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParameterDao {
    // region Single
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: ParameterEntity): Long

    @Update
    suspend fun update(parameter: ParameterEntity)

    @Query("SELECT * FROM parameter WHERE id = :id")
    suspend fun getById(id: Int): ParameterEntity?

    @Query("SELECT * FROM parameter WHERE id = :id")
    fun getFlowById(id: Int): Flow<ParameterEntity>

    //endregion Single
    //region List
    @Query(
        """
        SELECT * FROM parameter
        ORDER BY LOWER(name), name
        """
    )
    fun getFlowList(): Flow<List<ParameterEntity>>
    @Query(
        """
        SELECT * FROM parameter
        ORDER BY LOWER(name), name
        """
    )
    fun getList(): List<ParameterEntity>

    @Query(
        """
        SELECT * FROM parameter
        WHERE 
            searchName LIKE '%' || :name || '%'
        ORDER BY LOWER(name), name
    """
    )
    fun getFlowListByName(name: String): Flow<List<ParameterEntity>>

    @Query(
        """
        DELETE FROM parameter WHERE id IN (:ids)
    """
    )
    suspend fun deleteByIds(ids: List<Int>)

    //endregion List
    //region Parameter Sets
    /**
     * Gets all parameters NOT in the parameter set
     */
    @Query(
        """
        SELECT parameter.* FROM parameter
        LEFT JOIN parameter_set_parameter ON parameter.id = parameter_set_parameter.parameterId
            AND parameter_set_parameter.parameterSetId = :parameterSetId
        WHERE parameter_set_parameter.id IS NULL
        ORDER BY LOWER(parameter.name), parameter.name
        """
    )
    suspend fun getNotListBySetId(parameterSetId: Int): List<ParameterEntity>

    /**
     * Searches the parameters NOT in the parameter set
     */
    @Query(
        """
        SELECT parameter.* FROM parameter
        LEFT JOIN parameter_set_parameter ON parameter.id = parameter_set_parameter.parameterId
            AND parameter_set_parameter.parameterSetId = :parameterSetId
        WHERE
            parameter_set_parameter.id IS NULL
            AND parameter.searchName LIKE '%' || :name || '%'
        ORDER BY LOWER(name), name
        """
    )
    suspend fun getNotListBySetIdAndName(parameterSetId: Int, name: String): List<ParameterEntity>

    /**
     * Gets all the parameters in the parameter set
     */
    @Query(
        """
        SELECT parameter.* FROM parameter_set_parameter
        INNER JOIN parameter ON parameter_set_parameter.parameterId = parameter.id
        WHERE parameter_set_parameter.parameterSetId = :parameterSetId
        ORDER BY LOWER(parameter.name), parameter.name
        """
    )
    fun getFlowListBySetId(parameterSetId: Int): Flow<List<ParameterEntity>>

    /**
     * Searches the parameters in the parameter set
     */
    @Query(
        """
        SELECT parameter.* FROM parameter_set_parameter
        INNER JOIN parameter ON parameter_set_parameter.parameterId = parameter.id
        WHERE
            parameter_set_parameter.parameterSetId = :parameterSetId
            AND parameter.searchName LIKE '%' || :name || '%'
        ORDER BY LOWER(name), name
        """
    )
    suspend fun getListBySetIdAndName(parameterSetId: Int, name: String): List<ParameterEntity>

    //endregion Parameter Sets
    // region Other
    @Query(
        """
        SELECT (
            CASE
                WHEN EXISTS (
                    SELECT 1
                    FROM parameter
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