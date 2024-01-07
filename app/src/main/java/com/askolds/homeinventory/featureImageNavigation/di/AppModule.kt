package com.askolds.homeinventory.featureImageNavigation.di

import com.askolds.homeinventory.core.data.Database
import com.askolds.homeinventory.featureImageNavigation.data.repository.ImageThingNavigationRepository
import com.askolds.homeinventory.featureImageNavigation.data.repository.ImageThingNavigationRepositoryImpl
import com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.usecase.ChangeImageThingNavigations
import com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.usecase.GetListByThingId
import com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.usecase.ImageThingNavigationUseCases
import com.askolds.homeinventory.featureThing.data.repository.ThingRepository
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
    fun provideImageThingNavigationRepository(
        database: Database
    ): ImageThingNavigationRepository = ImageThingNavigationRepositoryImpl(
        database.imageThingNavigationDao()
    )

    @Provides
    @Singleton
    fun provideImageThingNavigationUseCases(
        repository: ImageThingNavigationRepository,
        thingRepository: ThingRepository
    ): ImageThingNavigationUseCases  = ImageThingNavigationUseCases(
        changeImageThingNavigations = ChangeImageThingNavigations(repository, thingRepository),
        getListByThingId = GetListByThingId(repository)
    )
}