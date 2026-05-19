package com.kurostream.tv.data.remote.trailer

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface YouTubeApi {
    @GET("search")
    suspend fun searchTrailer(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 1,
        @Query("key") apiKey: String
    ): Response<YouTubeSearchResponse>
}

data class YouTubeSearchResponse(
    val items: List<YouTubeItem>? = null
)

data class YouTubeItem(
    val id: YouTubeVideoId? = null,
    val snippet: YouTubeSnippet? = null
)

data class YouTubeVideoId(
    val videoId: String? = null
)

data class YouTubeSnippet(
    val title: String? = null,
    val description: String? = null,
    val thumbnails: YouTubeThumbnails? = null,
    val channelTitle: String? = null
)

data class YouTubeThumbnails(
    val `default`: YouTubeThumbnail? = null,
    val medium: YouTubeThumbnail? = null,
    val high: YouTubeThumbnail? = null,
    val maxres: YouTubeThumbnail? = null
)

data class YouTubeThumbnail(
    val url: String? = null,
    val width: Int? = null,
    val height: Int? = null
)

interface TrailerExtension {
    suspend fun getTrailer(animeId: String, title: String): TrailerSource?
}

data class TrailerSource(
    val id: String,
    val title: String,
    val url: String,
    val provider: String,
    val thumbnail: String? = null,
    val duration: Int? = null,
    val language: String? = null
)
