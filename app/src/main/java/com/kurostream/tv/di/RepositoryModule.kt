package com.kurostream.tv.di

import com.kurostream.tv.data.remote.anilist.AniListSyncManager
import com.kurostream.tv.data.local.profile.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideProfileRepository(
        profileRepository: ProfileRepository
    ): ProfileRepository {
        return profileRepository
    }

    @Provides
    @Singleton
    fun provideAniListSyncManager(
        anilistSyncManager: AniListSyncManager
    ): AniListSyncManager {
        return anilistSyncManager
    }
}
