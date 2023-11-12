package com.askolds.homeinventory.featureObject.data.repository

import com.askolds.homeinventory.featureHome.data.dao.HomeDao
import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import kotlinx.coroutines.flow.Flow

class ThingRepositoryImpl(
    private val dao: HomeDao
) : ThingRepository {
    override fun getList(): Flow<List<HomeEntity>> {
        return dao.getList()
    }

    override suspend fun insert(home: HomeEntity) {
        dao.insert(home)
    }

    override suspend fun getById(id: Int): HomeEntity? {
        return dao.getById(id)
    }

    override suspend fun nameExists(name: String, excludeName: String?): Boolean {
        return dao.nameExists(name, excludeName)
    }
}