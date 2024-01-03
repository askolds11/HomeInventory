package com.askolds.homeinventory.featureParameter.data.repository

import com.askolds.homeinventory.featureParameter.data.model.ParameterSetEntity
import com.askolds.homeinventory.featureParameter.data.model.ThingParameterSetEntity
import kotlinx.coroutines.flow.Flow

interface ThingParameterSetRepository {
    suspend fun insert(item: ThingParameterSetEntity): Int
    suspend fun insertAll(items: List<ThingParameterSetEntity>)
    fun getListByThingId(thingId: Int): Flow<List<ParameterSetEntity>>

    /**
     * returns map parameterSetId -> thingParameterSetId
     */
    fun getIdsByThingId(thingId: Int): Map<Int, Int>
    suspend fun deleteByIds(ids: List<Int>)
    suspend fun deleteByThingIdAndSetIds(thingId: Int, parameterSetIds: List<Int>)
}