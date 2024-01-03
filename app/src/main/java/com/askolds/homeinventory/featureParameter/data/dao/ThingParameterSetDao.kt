package com.askolds.homeinventory.featureParameter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetEntity
import com.askolds.homeinventory.featureParameter.data.model.ThingParameterSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThingParameterSetDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: ThingParameterSetEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(items: List<ThingParameterSetEntity>)

    @Query(
        """
        SELECT parameter_set.*
        FROM parameter_set
        JOIN thing_parameter_set ON thing_parameter_set.parameterSetId = parameter_set.id
        WHERE thing_parameter_set.thingId = :thingId
        ORDER BY LOWER(parameter_set.name), parameter_set.name
        """
    )
    fun getListByThingId(thingId: Int): Flow<List<ParameterSetEntity>>

    @Query(
        """
        SELECT parameterSetId, id
        FROM thing_parameter_set
        WHERE thingId = :thingId
        """
    )
    fun getIdsByThingId(
        thingId: Int
    ): Map<@MapColumn(columnName = "parameterSetId") Int,
            @MapColumn(columnName = "id") Int>

    @Query(
        """
        DELETE FROM thing_parameter_set WHERE id IN (:ids)
        """
    )
    suspend fun deleteByIds(ids: List<Int>)
    @Query(
        """
        DELETE
        FROM thing_parameter_set
        WHERE thingId = :thingId
            AND parameterSetId IN (:parameterSetIds)
        """
    )
    suspend fun deleteByThingIdAndSetIds(thingId: Int, parameterSetIds: List<Int>)
}