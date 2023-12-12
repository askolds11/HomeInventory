package com.askolds.homeinventory.featureHome.data.repository

import com.askolds.homeinventory.featureHome.data.dao.HomeDao
import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(
    private val dao: HomeDao
) : HomeRepository {
    override fun getList(): Flow<List<HomeEntity>> {
        return dao.getList()
    }

    override suspend fun insert(home: HomeEntity): Int {
        return dao.insert(home).toInt()
    }

    override suspend fun update(home: HomeEntity) {
        dao.update(home)
    }

    override suspend fun getById(id: Int): HomeEntity? {
        return dao.getById(id)
    }

    override fun getFlowById(id: Int): Flow<HomeEntity> {
        return dao.getFlowById(id)
    }

    override suspend fun nameExists(name: String, excludeName: String?): Boolean {
        return dao.nameExists(name, excludeName)
    }

    override fun search(name: String): Flow<List<HomeEntity>> {
        return dao.search(name)
    }

    override suspend fun deleteByIds(ids: List<Int>) {
        dao.deleteByIds(ids)
    }

    override suspend fun getImageIdsByIds(ids: List<Int>): List<Int?> {
        return dao.getImageIdsByIds(ids)
    }
}