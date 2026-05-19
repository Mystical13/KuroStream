package com.kurostream.tv.domain.repository

import com.kurostream.tv.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun getFeaturedAnime(): Flow<List<Anime>>
    suspend fun searchAnime(query: String): Flow<List<Anime>>
    suspend fun getAnimeById(id: String): Flow<Anime?>
    suspend fun getAnimeByMalId(malId: String): Flow<Anime?>
    suspend fun getAnimeByAnilistId(anilistId: String): Flow<Anime?>
    suspend fun getAnimeByStudio(studio: String): Flow<List<Anime>>
    suspend fun getAnimeByGenre(genre: String): Flow<List<Anime>>
    suspend fun getAnimeByYear(year: Int): Flow<List<Anime>>
}
