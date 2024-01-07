package com.askolds.homeinventory.featureParameter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetEntity
import com.askolds.homeinventory.featureParameter.data.model.ThingParameterParameterSetEntity
import kotlinx.coroutines.flow.Flow

typealias ThingParameterData = Map<ThingParameterParameterSetEntity, ParameterEntity>
typealias ThingParameterDataEntry = Map.Entry<ThingParameterParameterSetEntity, ParameterEntity>
typealias ThingParameterSetData = Map<ParameterSetEntity, ThingParameterData>
typealias ThingParameterSetDataEntry = Map.Entry<ParameterSetEntity, ThingParameterData>


@Dao
interface ThingParameterParameterSetDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: ThingParameterParameterSetEntity): Long
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(items: List<ThingParameterParameterSetEntity>)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(item: ThingParameterParameterSetEntity)
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateAll(items: List<ThingParameterParameterSetEntity>)
    @Query(
        """
        SELECT thing_parameter_parameter_set.*, parameter.*
        FROM thing_parameter_parameter_set
        INNER JOIN parameter ON parameter.id = thing_parameter_parameter_set.parameterId
        WHERE thing_parameter_parameter_set.thingId = :thingId
            AND thing_parameter_parameter_set.thingParameterSetId IS NULL
        ORDER BY LOWER(parameter.name), parameter.name
        """)
    fun getListWithoutSetByThingId(thingId: Int): Flow<ThingParameterData>

    @Query(
        """
        SELECT parameter_set.*, thing_parameter_parameter_set.*, parameter.*
        FROM thing_parameter_parameter_set
        INNER JOIN thing_parameter_set ON thing_parameter_set.id = thing_parameter_parameter_set.thingParameterSetId
        INNER JOIN parameter_set ON parameter_set.id = thing_parameter_set.parameterSetId
        INNER JOIN parameter_set_parameter ON parameter_set_parameter.id = thing_parameter_parameter_set.parameterSetParameterId
        INNER JOIN parameter ON parameter.id = parameter_set_parameter.parameterId
        WHERE thing_parameter_parameter_set.thingId = :thingId
            AND thing_parameter_parameter_set.thingParameterSetId IS NOT NULL
        ORDER BY LOWER(parameter.name), parameter.name
        """)
    fun getListGroupedBySetByThingId(thingId: Int): Flow<ThingParameterSetData>

    @Query(
        """
        SELECT thing_parameter_parameter_set.*, parameter.*
        FROM thing_parameter_parameter_set
        INNER JOIN parameter_set_parameter ON parameter_set_parameter.id = thing_parameter_parameter_set.parameterSetParameterId
        INNER JOIN parameter ON parameter.id = parameter_set_parameter.parameterId
        INNER JOIN thing_parameter_set ON thing_parameter_set.id = thing_parameter_parameter_set.thingParameterSetId
        WHERE thing_parameter_parameter_set.thingId = :thingId
            AND thing_parameter_set.parameterSetId = :setId
        ORDER BY LOWER(parameter.name), parameter.name
        """)
    fun getListByThingIdAndSetId(thingId: Int, setId: Int): Flow<ThingParameterData>

    @Query("""
        DELETE FROM thing_parameter_parameter_set WHERE id IN (:ids)
    """)
    suspend fun deleteByIds(ids: List<Int>)
}