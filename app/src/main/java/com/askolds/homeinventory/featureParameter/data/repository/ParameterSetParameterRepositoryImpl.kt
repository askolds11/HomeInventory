package com.askolds.homeinventory.featureParameter.data.repository

import com.askolds.homeinventory.featureParameter.data.dao.ParameterDao
import com.askolds.homeinventory.featureParameter.data.dao.ParameterSetParameterDao
import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetParameterEntity
import kotlinx.coroutines.flow.Flow

class ParameterSetParameterRepositoryImpl(
    private val dao: ParameterSetParameterDao,
    private val parameterDao: ParameterDao
) : ParameterSetParameterRepository {
    override suspend fun insertAll(parameterSetParameterList: Collection<ParameterSetParameterEntity>) {
        dao.insertAll(parameterSetParameterList)
    }

    override suspend fun getNotListBySetId(parameterSetId: Int): List<ParameterEntity> {
        return parameterDao.getNotListBySetId(parameterSetId)
    }

    override fun getFlowListBySetId(parameterSetId: Int): Flow<List<ParameterEntity>> {
        return parameterDao.getFlowListBySetId(parameterSetId)
    }

    override suspend fun getNotListBySetIdAndName(parameterSetId: Int, name: String): List<ParameterEntity> {
        return parameterDao.getNotListBySetIdAndName(parameterSetId, name)
    }

    override suspend fun getListBySetIdAndName(parameterSetId: Int, name: String): List<ParameterEntity> {
        return parameterDao.getListBySetIdAndName(parameterSetId, name)
    }

    override suspend fun deleteByIds(ids: Collection<Int>) {
        dao.deleteByIds(ids)
    }

    override suspend fun deleteByParameterIds(ids: Collection<Int>) {
        dao.deleteByParameterIds(ids)
    }

    override fun getIdsBySetId(parameterSetId: Int): Map<Int, Int> {
        return dao.getIdsBySetId(parameterSetId)
    }
}