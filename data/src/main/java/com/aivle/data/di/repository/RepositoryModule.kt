package com.aivle.data.di.repository

import com.aivle.data.di.api.FuniBuniApiQualifier
import com.aivle.data.di.api.FuniBuniSignWithTokenApiQualifier
import com.aivle.data.repository.AccessTokenRepositoryImpl
import com.aivle.data.repository.AddressRepositoryImpl
import com.aivle.data.repository.KakaoAddressRepositoryImpl
import com.aivle.data.repository.SharingPostRepositoryImpl
import com.aivle.data.repository.SignRepositoryImpl
import com.aivle.data.repository.UserRepositoryImpl
import com.aivle.data.repository.RefreshTokenRepositoryImpl
import com.aivle.domain.repository.AccessTokenRepository
import com.aivle.domain.repository.AddressRepository
import com.aivle.domain.repository.KakaoAddressRepository
import com.aivle.domain.repository.SharingPostRepository
import com.aivle.domain.repository.SignRepository
import com.aivle.domain.repository.UserRepository
import com.aivle.domain.repository.RefreshTokenRepository
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
    fun bindRefreshTokenRepositoryImpl(
        repositoryImpl: RefreshTokenRepositoryImpl
    ): RefreshTokenRepository

    @Binds
    fun bindAccessTokenRepositoryImpl(
        repositoryImpl: AccessTokenRepositoryImpl
    ): AccessTokenRepository

    @Binds
    fun bindSharingPostRepositoryImpl(
        repository: SharingPostRepositoryImpl
    ): SharingPostRepository

    @Binds
    fun bindKakaoAddressRepositoryImpl(
        repository: KakaoAddressRepositoryImpl
    ): KakaoAddressRepository
}