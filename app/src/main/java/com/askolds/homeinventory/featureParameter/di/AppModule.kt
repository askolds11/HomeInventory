package com.askolds.homeinventory.featureParameter.di

import com.askolds.homeinventory.data.Database
import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepository
import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepositoryImpl
import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetParameterRepository
import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetParameterRepositoryImpl
import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetRepository
import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetRepositoryImpl
import com.askolds.homeinventory.featureParameter.data.repository.ThingParameterParameterSetRepository
import com.askolds.homeinventory.featureParameter.data.repository.ThingParameterParameterSetRepositoryImpl
import com.askolds.homeinventory.featureParameter.data.repository.ThingParameterSetRepository
import com.askolds.homeinventory.featureParameter.data.repository.ThingParameterSetRepositoryImpl
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.ParameterUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.ParameterSetUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSetParameter.ParameterSetParameterUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet.ChangeThingParameters
import com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet.GetListGroupedBySetByThingId
import com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet.GetListWithoutSetByThingId
import com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet.ThingParameterParameterSetUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterSet.ChangeThingParameterSets
import com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterSet.ThingParameterSetUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.Add as Parameter_Add
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.DeleteList as Parameter_DeleteList
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.Get as Parameter_Get
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.GetFlow as Parameter_GetFlow
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.GetList as Parameter_GetList
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.GetListFlow as Parameter_GetListFlow
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.Search as Parameter_Search
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.Update as Parameter_Update
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.validation.ValidateName as Parameter_ValidateName
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.Add as ParameterSet_Add
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.DeleteList as ParameterSet_DeleteList
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.Get as ParameterSet_Get
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.GetFlow as ParameterSet_GetFlow
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.GetListFlow as ParameterSet_GetListFlow
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.Search as ParameterSet_Search
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.Update as ParameterSet_Update
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.validation.ValidateName as ParameterSet_ValidateName
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSetParameter.GetAllParameterList as ParameterSetParameter_GetAllParameterList
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSetParameter.GetFlowParameterList as ParameterSetParameter_GetFlowParameterList

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideParameterRepository(database: Database): ParameterRepository =
        ParameterRepositoryImpl(
            database.parameterDao()
        )

    @Provides
    @Singleton
    fun provideParameterSetRepository(database: Database): ParameterSetRepository =
        ParameterSetRepositoryImpl(
            database.parameterSetDao()
        )

    @Provides
    @Singleton
    fun provideParameterSetParameterRepository(database: Database): ParameterSetParameterRepository =
        ParameterSetParameterRepositoryImpl(
            dao = database.parameterSetParameterDao(),
            parameterDao = database.parameterDao()
        )

    @Provides
    @Singleton
    fun provideThingParameterSetRepository(
        database: Database
    ): ThingParameterSetRepository = ThingParameterSetRepositoryImpl(
        database.thingParameterSetDao()
    )

    @Provides
    @Singleton
    fun provideThingParameterParameterSetRepository(
        database: Database
    ): ThingParameterParameterSetRepository = ThingParameterParameterSetRepositoryImpl(
        database.thingParameterParameterSetDao()
    )

    @Provides
    @Singleton
    fun provideParameterUseCases(repository: ParameterRepository): ParameterUseCases =
        ParameterUseCases(
            getList = Parameter_GetList(repository),
            getListFlow = Parameter_GetListFlow(repository),
            get = Parameter_Get(repository),
            getFlow = Parameter_GetFlow(repository),
            add = Parameter_Add(repository),
            update = Parameter_Update(repository),
            validateName = Parameter_ValidateName(repository),
            search = Parameter_Search(repository),
            deleteList = Parameter_DeleteList(repository)
        )

    @Provides
    @Singleton
    fun provideParameterSetUseCases(
        repository: ParameterSetRepository,
        parameterSetParameterRepository: ParameterSetParameterRepository
    ): ParameterSetUseCases =
        ParameterSetUseCases(
            getListFlow = ParameterSet_GetListFlow(repository),
            get = ParameterSet_Get(repository),
            getFlow = ParameterSet_GetFlow(repository),
            add = ParameterSet_Add(repository, parameterSetParameterRepository),
            update = ParameterSet_Update(repository, parameterSetParameterRepository),
            validateName = ParameterSet_ValidateName(
                repository
            ),
            search = ParameterSet_Search(repository),
            deleteList = ParameterSet_DeleteList(repository)
        )

    @Provides
    @Singleton
    fun provideParameterSetParameterUseCases(
        repository: ParameterSetParameterRepository,
    ): ParameterSetParameterUseCases =
        ParameterSetParameterUseCases(
            getAllParameterList = ParameterSetParameter_GetAllParameterList(repository),
            getFlowParameterList = ParameterSetParameter_GetFlowParameterList(repository)
        )

    @Provides
    @Singleton
    fun provideThingParameterSetUseCases(
        repository: ThingParameterSetRepository,
        thingParameterParameterSetRepository: ThingParameterParameterSetRepository,
        parameterSetParameterRepository: ParameterSetParameterRepository
    ): ThingParameterSetUseCases = ThingParameterSetUseCases(
        changeThingParameterSets = ChangeThingParameterSets(
            repository,
            thingParameterParameterSetRepository,
            parameterSetParameterRepository
        )
    )

    @Provides
    @Singleton
    fun provideThingParameterParameterSetUseCases(
        repository: ThingParameterParameterSetRepository
    ): ThingParameterParameterSetUseCases = ThingParameterParameterSetUseCases(
        changeThingParameters = ChangeThingParameters(repository),
        getListWithoutSetByThingId = GetListWithoutSetByThingId(repository),
        getListGroupedBySetByThingId = GetListGroupedBySetByThingId(repository),
    )
}