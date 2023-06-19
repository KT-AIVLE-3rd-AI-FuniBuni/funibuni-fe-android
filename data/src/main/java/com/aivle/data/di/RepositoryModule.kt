package com.aivle.data.di

import com.aivle.data.repository.AddressRepositoryImpl
import com.aivle.data.repository.SignRepositoryImpl
import com.aivle.data.repository.UserRepositoryImpl
import com.aivle.data.repository.WebTokenRepositoryImpl
import com.aivle.domain.repository.AddressRepository
import com.aivle.domain.repository.SignRepository
import com.aivle.domain.repository.UserRepository
import com.aivle.domain.repository.WebTokenRepository
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

    @Binds
    fun bindSignRepositoryImpl(
        repositoryImpl: SignRepositoryImpl
    ): SignRepository

    @Binds
    fun bindUserRepositoryImpl(
        repositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    fun bindWebTokenRepositoryImpl(
        repositoryImpl: WebTokenRepositoryImpl
    ): WebTokenRepository
}