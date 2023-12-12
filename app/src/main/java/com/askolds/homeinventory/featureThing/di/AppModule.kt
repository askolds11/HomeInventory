package com.askolds.homeinventory.featureThing.di

import com.askolds.homeinventory.data.Database
import com.askolds.homeinventory.featureHome.domain.usecase.*
import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureThing.data.repository.ThingRepository
import com.askolds.homeinventory.featureThing.data.repository.ThingRepositoryImpl
import com.askolds.homeinventory.featureThing.domain.usecase.*
import com.askolds.homeinventory.featureThing.domain.usecase.Add
import com.askolds.homeinventory.featureThing.domain.usecase.DeleteList
import com.askolds.homeinventory.featureThing.domain.usecase.Get
import com.askolds.homeinventory.featureThing.domain.usecase.GetFlow
import com.askolds.homeinventory.featureThing.domain.usecase.GetListFlow
import com.askolds.homeinventory.featureThing.domain.usecase.Search
import com.askolds.homeinventory.featureThing.domain.usecase.Update
import com.askolds.homeinventory.featureThing.domain.usecase.validation.ValidateName
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
    fun provideThingRepository(database: Database): ThingRepository = ThingRepositoryImpl(
        database.thingDao()
    )

    @Provides
    @Singleton
    fun provideThingUseCases(repository: ThingRepository, imageRepository: ImageRepository): ThingUseCases  = ThingUseCases(
        get = Get(repository, imageRepository),
        getFlow = GetFlow(repository, imageRepository),
        getListFlow = GetListFlow(repository, imageRepository),
        add = Add(repository, imageRepository),
        update = Update(repository, imageRepository),
        search = Search(repository),
        deleteList = DeleteList(repository, imageRepository),
        validateName = ValidateName()
    )
}