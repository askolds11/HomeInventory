package com.askolds.homeinventory.featureParameter.data.repository

import com.askolds.homeinventory.featureParameter.data.dao.ParameterSetDao
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetEntity
import kotlinx.coroutines.flow.Flow

class ParameterSetRepositoryImpl(
    private val dao: ParameterSetDao
) : ParameterSetRepository {
    override fun getList(): Flow<List<ParameterSetEntity>> {
        return dao.getFlowList()
    }

    override suspend fun insert(parameterSet: ParameterSetEntity): Int {
        return dao.insert(parameterSet).toInt()
    }

    override suspend fun update(home: ParameterSetEntity) {
        dao.update(home)
    }

    override suspend fun getById(id: Int): ParameterSetEntity? {
        return dao.getById(id)
    }

    override fun getFlowById(id: Int): Flow<ParameterSetEntity> {
        return dao.getFlowById(id)
    }

    override suspend fun nameExists(name: String, excludeName: String?): Boolean {
        return dao.nameExists(name, excludeName)
    }

    override fun search(name: String): Flow<List<ParameterSetEntity>> {
        return dao.getFlowListByName(name)
    }

    override suspend fun deleteByIds(ids: List<Int>) {
        dao.deleteByIds(ids)
    }
}