package com.livly.booking.di

import com.livly.booking.FeatureFlagManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeatureModule {

    @Provides
    @Singleton
    fun provideFeatureFlagManager(): FeatureFlagManager {
        return FeatureFlagManager()
    }
}