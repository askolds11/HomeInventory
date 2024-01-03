package com.askolds.homeinventory.featureParameter.data.repository

import com.askolds.homeinventory.featureParameter.data.dao.ThingParameterData
import com.askolds.homeinventory.featureParameter.data.dao.ThingParameterParameterSetDao
import com.askolds.homeinventory.featureParameter.data.dao.ThingParameterSetData
import com.askolds.homeinventory.featureParameter.data.model.ThingParameterParameterSetEntity
import kotlinx.coroutines.flow.Flow

class ThingParameterParameterSetRepositoryImpl(
    private val dao: ThingParameterParameterSetDao
): ThingParameterParameterSetRepository {
    override suspend fun insert(item: ThingParameterParameterSetEntity): Int {
        return dao.insert(item).toInt()
    }

    override suspend fun insertAll(items: List<ThingParameterParameterSetEntity>) {
        dao.insertAll(items)
    }

    override suspend fun update(item: ThingParameterParameterSetEntity) {
        dao.update(item)
    }

    override suspend fun updateAll(items: List<ThingParameterParameterSetEntity>) {
        dao.updateAll(items)
    }

    override fun getListWithoutSetByThingId(thingId: Int): Flow<ThingParameterData> {
        return dao.getListWithoutSetByThingId(thingId)
    }

    override fun getListGroupedBySetByThingId(thingId: Int): Flow<ThingParameterSetData> {
        return dao.getListGroupedBySetByThingId(thingId)
    }

    override fun getListByThingIdAndSetId(thingId: Int, parameterSetId: Int): Flow<ThingParameterData> {
        return dao.getListByThingIdAndSetId(thingId, parameterSetId)
    }

    override suspend fun deleteByIds(ids: List<Int>) {
        dao.deleteByIds(ids)
    }
}