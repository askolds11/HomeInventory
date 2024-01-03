package com.askolds.homeinventory.featureParameter.data.repository

import com.askolds.homeinventory.featureParameter.data.dao.ThingParameterSetDao
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetEntity
import com.askolds.homeinventory.featureParameter.data.model.ThingParameterSetEntity
import kotlinx.coroutines.flow.Flow

class ThingParameterSetRepositoryImpl(
    private val dao: ThingParameterSetDao
) : ThingParameterSetRepository {
    override suspend fun insert(item: ThingParameterSetEntity): Int {
        return dao.insert(item).toInt()
    }

    override suspend fun insertAll(items: List<ThingParameterSetEntity>) {
        dao.insertAll(items)
    }

    override fun getListByThingId(thingId: Int): Flow<List<ParameterSetEntity>> {
        return dao.getListByThingId(thingId)
    }

    override fun getIdsByThingId(thingId: Int): Map<Int, Int> {
        return dao.getIdsByThingId(thingId)
    }

    override suspend fun deleteByIds(ids: List<Int>) {
        dao.deleteByIds(ids)
    }

    override suspend fun deleteByThingIdAndSetIds(thingId: Int, parameterSetIds: List<Int>) {
        dao.deleteByThingIdAndSetIds(thingId, parameterSetIds)
    }
}