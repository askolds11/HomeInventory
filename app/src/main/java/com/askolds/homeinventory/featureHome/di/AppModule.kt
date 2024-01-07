package com.askolds.homeinventory.featureHome.di

import com.askolds.homeinventory.core.data.Database
import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import com.askolds.homeinventory.featureHome.data.repository.HomeRepositoryImpl
import com.askolds.homeinventory.featureHome.domain.usecase.*
import com.askolds.homeinventory.featureHome.domain.usecase.home.Add
import com.askolds.homeinventory.featureHome.domain.usecase.home.DeleteList
import com.askolds.homeinventory.featureHome.domain.usecase.home.GetFlow
import com.askolds.homeinventory.featureHome.domain.usecase.home.GetListFlow
import com.askolds.homeinventory.featureHome.domain.usecase.home.HomeUseCases
import com.askolds.homeinventory.featureHome.domain.usecase.home.Search
import com.askolds.homeinventory.featureHome.domain.usecase.home.Update
import com.askolds.homeinventory.featureHome.domain.usecase.home.validation.ValidateName
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
    fun provideHomeUseCases(repository: HomeRepository, imageRepository: ImageRepository): HomeUseCases = HomeUseCases(
        getListFlow = GetListFlow(repository, imageRepository),
        getFlow = GetFlow(repository, imageRepository),
        add = Add(repository, imageRepository),
        update = Update(repository, imageRepository),
        validateName = ValidateName(repository),
        search = Search(repository, imageRepository),
        deleteList = DeleteList(repository, imageRepository)
    )
}