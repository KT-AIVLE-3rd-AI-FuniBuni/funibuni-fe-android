package com.aivle.data.di.dataSource

import com.aivle.data.datastore.PreferencesDatastore
import com.aivle.data.datastore.PreferencesDatastoreImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface LocalDataSourceModule {

    @Binds
    fun providePreferencesDatastore(
        datastoreImpl: PreferencesDatastoreImpl
    ): PreferencesDatastore
}