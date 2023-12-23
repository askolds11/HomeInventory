package com.askolds.homeinventory.featureParameter.data.repository

import com.askolds.homeinventory.featureParameter.data.dao.ParameterDao
import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity
import kotlinx.coroutines.flow.Flow

class ParameterRepositoryImpl(
    private val dao: ParameterDao
) : ParameterRepository {
    override suspend fun insert(parameter: ParameterEntity): Int {
        return dao.insert(parameter).toInt()
    }
    override suspend fun update(parameter: ParameterEntity) {
        dao.update(parameter)
    }
    override fun getList(): Flow<List<ParameterEntity>> {
        return dao.getList()
    }
//
//    override suspend fun getById(id: Int): HomeEntity? {
//        return dao.getById(id)
//    }
//
//    override fun getFlowById(id: Int): Flow<HomeEntity> {
//        return dao.getFlowById(id)
//    }
//
    override suspend fun nameExists(name: String, excludeName: String?): Boolean {
        return dao.nameExists(name, excludeName)
    }
//
//    override fun search(name: String): Flow<List<HomeEntity>> {
//        return dao.search(name)
//    }

    override suspend fun deleteByIds(ids: List<Int>) {
        dao.deleteByIds(ids)
    }

//    override suspend fun getImageIdsByIds(ids: List<Int>): List<Int?> {
//        return dao.getImageIdsByIds(ids)
//    }
}