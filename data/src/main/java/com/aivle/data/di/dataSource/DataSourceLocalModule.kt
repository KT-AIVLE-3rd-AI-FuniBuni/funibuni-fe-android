package com.aivle.data.di.dataSource

import com.aivle.data.datastore.PreferencesDataStore
import com.aivle.data.datastore.PreferencesDataStoreImpl
import com.aivle.data.di.api.FuniBuniApiQualifier
import com.aivle.data.di.api.FuniBuniSignWithTokenApiQualifier
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