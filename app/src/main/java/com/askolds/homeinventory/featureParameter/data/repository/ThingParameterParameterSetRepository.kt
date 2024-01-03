package com.askolds.homeinventory.featureParameter.data.repository

import com.askolds.homeinventory.featureParameter.data.dao.ThingParameterData
import com.askolds.homeinventory.featureParameter.data.dao.ThingParameterSetData
import com.askolds.homeinventory.featureParameter.data.model.ThingParameterParameterSetEntity
import kotlinx.coroutines.flow.Flow

interface ThingParameterParameterSetRepository {
    suspend fun insert(item: ThingParameterParameterSetEntity): Int
    suspend fun insertAll(items: List<ThingParameterParameterSetEntity>)
    suspend fun update(item: ThingParameterParameterSetEntity)
    suspend fun updateAll(items: List<ThingParameterParameterSetEntity>)
    fun getListWithoutSetByThingId(thingId: Int): Flow<ThingParameterData>
    fun getListGroupedBySetByThingId(thingId: Int): Flow<ThingParameterSetData>
    fun getListByThingIdAndSetId(thingId: Int, parameterSetId: Int): Flow<ThingParameterData>
    suspend fun deleteByIds(ids: List<Int>)
}