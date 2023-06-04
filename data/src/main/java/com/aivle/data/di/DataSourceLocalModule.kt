package com.aivle.data.di

import com.aivle.data.datastore.PreferencesDataStore
import com.aivle.data.datastore.PreferencesDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceLocalModule {

    @Binds
    @Singleton
    abstract fun bindPreferencesDataStore(
        dataStore: PreferencesDataStoreImpl
    ): PreferencesDataStore
}