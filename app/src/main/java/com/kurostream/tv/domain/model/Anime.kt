package com.kurostream.tv.domain.model

data class Anime(
    val id: String,
    val title: String,
    val episodes: List<Episode>,
    val malId: String? = null,
    val anilistId: String? = null,
    val posterUrl: String? = null,
    val rating: Float? = null,
    val status: String? = null,
    val description: String? = null,
    val genres: List<String>? = null,
    val studio: String? = null,
    val year: Int? = null,
    val trailerUrl: String? = null
)

data class Episode(
    val id: String,
    val title: String? = null,
    val episodeNumber: Int,
    val seasonNumber: Int? = null,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val duration: Long? = null,
    val releaseDate: String? = null
)
