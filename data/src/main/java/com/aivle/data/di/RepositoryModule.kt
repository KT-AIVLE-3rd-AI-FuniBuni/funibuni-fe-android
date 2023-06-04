package com.aivle.data.di

import com.aivle.data.repository.AddressRepositoryImpl
import com.aivle.domain.repository.AddressRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindAddressRepository(
        repositoryImpl: AddressRepositoryImpl
    ): AddressRepository
}