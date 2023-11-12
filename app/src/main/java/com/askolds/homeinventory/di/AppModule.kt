package com.askolds.homeinventory.di

import android.content.Context
import androidx.room.Room
import com.askolds.homeinventory.data.Database
import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import com.askolds.homeinventory.featureHome.data.repository.HomeRepositoryImpl
import com.askolds.homeinventory.featureHome.domain.usecase.Add
import com.askolds.homeinventory.featureHome.domain.usecase.GetList
import com.askolds.homeinventory.featureHome.domain.usecase.HomeUseCases
import com.askolds.homeinventory.featureHome.domain.usecase.Search
import com.askolds.homeinventory.featureHome.domain.usecase.validation.ValidateName
import com.askolds.homeinventory.ui.navigation.appbars.AppBarState
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            Database.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    @Named("bottomBarState")
    fun provideBottomBarState(): AppBarState = AppBarState(
        -Float.MAX_VALUE,
        0f,
        0f
    )

    @Provides
    @Singleton
    @Named("topBarState")
    fun provideTopBarState(): AppBarState = AppBarState(
        -Float.MAX_VALUE,
        0f,
        0f
    )

    @Provides
    @Singleton
    fun provideAppBarsState(
        @Named("topBarState") topBarState: AppBarState,
        @Named("bottomBarState") bottomBarState: AppBarState
    ): AppBarsState = AppBarsState(topBarState, bottomBarState)

    @Provides
    @Singleton
    fun provideHomeRepository(database: Database): HomeRepository = HomeRepositoryImpl(
        database.homeDao()
    )

    @Provides
    @Singleton
    fun provideHomeUseCases(repository: HomeRepository): HomeUseCases  = HomeUseCases(
        getList = GetList(repository),
        add = Add(repository),
        validateName = ValidateName(repository),
        search = Search(repository)
    )
}