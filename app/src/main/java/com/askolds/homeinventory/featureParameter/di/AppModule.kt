package com.askolds.homeinventory.featureParameter.di

import com.askolds.homeinventory.data.Database
import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepository
import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepositoryImpl
import com.askolds.homeinventory.featureParameter.domain.usecase.Add
import com.askolds.homeinventory.featureParameter.domain.usecase.DeleteList
import com.askolds.homeinventory.featureParameter.domain.usecase.GetListFlow
import com.askolds.homeinventory.featureParameter.domain.usecase.ParameterUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.validation.ValidateName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideParameterRepository(database: Database): ParameterRepository = ParameterRepositoryImpl(
        database.parameterDao()
    )

    @Provides
    @Singleton
    fun provideParameterUseCases(repository: ParameterRepository): ParameterUseCases  = ParameterUseCases(
        getListFlow = GetListFlow(repository),
//        get = Get(repository, imageRepository),
//        getFlow = GetFlow(repository, imageRepository),
        add = Add(repository),
//        update = Update(repository, imageRepository),
        validateName = ValidateName(repository),
//        search = Search(repository, imageRepository),
        deleteList = DeleteList(repository)
    )
}