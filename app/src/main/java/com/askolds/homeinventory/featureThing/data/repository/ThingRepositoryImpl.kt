package com.askolds.homeinventory.featureThing.data.repository

import com.askolds.homeinventory.featureThing.data.dao.ThingDao
import com.askolds.homeinventory.featureThing.data.model.ThingEntity
import kotlinx.coroutines.flow.Flow

class ThingRepositoryImpl(
    private val dao: ThingDao
) : ThingRepository {
    override fun getList(homeId: Int, parentId: Int?): Flow<List<ThingEntity>> {
        return dao.getList(homeId, parentId)
    }

    override fun getList(parentId: Int): Flow<List<ThingEntity>> {
        return dao.getList(parentId)
    }

    override suspend fun insert(thing: ThingEntity): Int {
        return dao.insert(thing).toInt()
    }

    override suspend fun update(thing: ThingEntity) {
        dao.update(thing)
    }

    override suspend fun getById(id: Int): ThingEntity? {
        return dao.getById(id)
    }

    override fun getFlowById(id: Int): Flow<ThingEntity> {
        return dao.getFlowById(id)
    }

    override fun search(homeId: Int?, parentId: Int?, name: String): Flow<List<ThingEntity>> {
        return dao.search(homeId, parentId, name)
    }

    override suspend fun deleteByIds(ids: List<Int>) {
        dao.deleteByIds(ids)
    }
}