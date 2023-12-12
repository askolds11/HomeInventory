package com.askolds.homeinventory.featureHome.di

import com.askolds.homeinventory.data.Database
import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import com.askolds.homeinventory.featureHome.data.repository.HomeRepositoryImpl
import com.askolds.homeinventory.featureHome.domain.usecase.*
import com.askolds.homeinventory.featureHome.domain.usecase.Add
import com.askolds.homeinventory.featureHome.domain.usecase.DeleteList
import com.askolds.homeinventory.featureHome.domain.usecase.Get
import com.askolds.homeinventory.featureHome.domain.usecase.GetFlow
import com.askolds.homeinventory.featureHome.domain.usecase.GetListFlow
import com.askolds.homeinventory.featureHome.domain.usecase.Search
import com.askolds.homeinventory.featureHome.domain.usecase.Update
import com.askolds.homeinventory.featureHome.domain.usecase.validation.ValidateName
import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureThing.domain.usecase.*
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
    fun provideHomeRepository(database: Database): HomeRepository = HomeRepositoryImpl(
        database.homeDao()
    )

    @Provides
    @Singleton
    fun provideHomeUseCases(repository: HomeRepository, imageRepository: ImageRepository): HomeUseCases  = HomeUseCases(
        getListFlow = GetListFlow(repository, imageRepository),
        get = Get(repository, imageRepository),
        getFlow = GetFlow(repository, imageRepository),
        add = Add(repository, imageRepository),
        update = Update(repository, imageRepository),
        validateName = ValidateName(repository),
        search = Search(repository, imageRepository),
        deleteList = DeleteList(repository, imageRepository)
    )
}