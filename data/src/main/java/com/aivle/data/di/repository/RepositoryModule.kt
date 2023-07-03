package com.aivle.data.di.repository

import com.aivle.data.repository.AccessTokenRepositoryImpl
import com.aivle.data.repository.AddressLocalRepositoryImpl
import com.aivle.data.repository.KakaoAddressRepositoryImpl
import com.aivle.data.repository.MyBuniRepositoryImpl
import com.aivle.data.repository.SharingPostRepositoryImpl
import com.aivle.data.repository.SignRepositoryImpl
import com.aivle.data.repository.UserRepositoryImpl
import com.aivle.data.repository.RefreshTokenRepositoryImpl
import com.aivle.data.repository.WasteRepositoryImpl
import com.aivle.domain.repository.AccessTokenRepository
import com.aivle.domain.repository.AddressLocalRepository
import com.aivle.domain.repository.KakaoAddressRepository
import com.aivle.domain.repository.MyBuniRepository
import com.aivle.domain.repository.SharingPostRepository
import com.aivle.domain.repository.SignRepository
import com.aivle.domain.repository.UserRepository
import com.aivle.domain.repository.RefreshTokenRepository
import com.aivle.domain.repository.WasteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindAddressLocalRepository(
        repositoryImpl: AddressLocalRepositoryImpl
    ): AddressLocalRepository

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
        repositoryImpl: SharingPostRepositoryImpl
    ): SharingPostRepository

    @Binds
    fun bindKakaoAddressRepositoryImpl(
        repositoryImpl: KakaoAddressRepositoryImpl
    ): KakaoAddressRepository

    @Binds
    fun bindWasteRepositoryImpl(
        repositoryImpl: WasteRepositoryImpl
    ): WasteRepository

    @Binds
    fun bindMyBuniRepositoryImpl(
        repositoryImpl: MyBuniRepositoryImpl
    ): MyBuniRepository
}