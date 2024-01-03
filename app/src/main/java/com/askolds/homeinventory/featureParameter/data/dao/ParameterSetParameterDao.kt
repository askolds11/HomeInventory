package com.askolds.homeinventory.featureParameter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetParameterEntity

@Dao
interface ParameterSetParameterDao {
    // region Single
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(parameterSetParameter: ParameterSetParameterEntity): Long

    @Update
    suspend fun update(parameterSetParameter: ParameterSetParameterEntity)
    // endregion Single
    // region List
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(parameterSetParameterList: Collection<ParameterSetParameterEntity>)

    @Query(
        """
        DELETE FROM parameter_set_parameter WHERE id IN (:ids)
        """
    )
    suspend fun deleteByIds(ids: Collection<Int>)

    @Query(
        """
        DELETE FROM parameter_set_parameter WHERE parameterId IN (:parameterIds)
        """
    )
    suspend fun deleteByParameterIds(parameterIds: Collection<Int>)
    // endregion List

    @Query(
        """
        SELECT parameterId, id
        FROM parameter_set_parameter
        WHERE parameterSetId = :parameterSetId
        """
    )
    fun getIdsBySetId(
        parameterSetId: Int
    ): Map<@MapColumn(columnName = "parameterId") Int,
            @MapColumn(columnName = "id") Int>


}