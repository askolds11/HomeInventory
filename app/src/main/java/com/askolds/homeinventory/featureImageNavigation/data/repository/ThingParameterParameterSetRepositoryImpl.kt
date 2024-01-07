package com.askolds.homeinventory.featureImageNavigation.data.repository

import com.askolds.homeinventory.featureImageNavigation.data.dao.ImageThingNavigationDao
import com.askolds.homeinventory.featureImageNavigation.data.model.ImageThingNavigationEntity
import kotlinx.coroutines.flow.Flow

class ImageThingNavigationRepositoryImpl(
    private val dao: ImageThingNavigationDao
): ImageThingNavigationRepository {
    override suspend fun insertAll(items: List<ImageThingNavigationEntity>) {
        dao.insertAll(items)
    }

    override suspend fun updateAll(items: List<ImageThingNavigationEntity>) {
        dao.updateAll(items)
    }

    override fun getListByThingId(thingId: Int): Flow<List<ImageThingNavigationEntity>> {
        return dao.getListByThingId(thingId)
    }

    override suspend fun deleteByIds(ids: List<Int>) {
        dao.deleteByIds(ids)
    }
}