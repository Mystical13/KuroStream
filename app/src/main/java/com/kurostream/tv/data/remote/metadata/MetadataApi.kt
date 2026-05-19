package com.kurostream.tv.data.remote.metadata

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import retrofit2.Response

interface CinemetaApi {
    @GET("meta/{type}/{id}.json")
    suspend fun getMeta(
        @Path("type") type: String,
        @Path("id") id: String
    ): Response<CinemetaResponse>
}

interface KitsuApi {
    @POST
    suspend fun query(
        @Url url: String,
        @Body query: GraphQLQuery
    ): Response<KitsuResponse>
}

data class GraphQLQuery(val query: String)

data class CinemetaResponse(
    val meta: CinemetaMeta? = null
)

data class CinemetaMeta(
    val id: String,
    val name: String,
    val poster: String? = null,
    val logo: String? = null,
    val genres: List<String>? = null,
    val year: Int? = null,
    val description: String? = null,
    val releaseInfo: String? = null,
    val runtime: Int? = null,
    val rating: CinemetaRating? = null,
    val trailer: String? = null,
    val videos: List<CinemetaVideo>? = null
)

data class CinemetaRating(
    val percentage: Int? = null,
    val votes: Int? = null
)
data class CinemetaVideo(
    val id: String,
    val name: String? = null,
    val season: Int? = null,
    val episode: Int? = null,
    val overview: String? = null,
    val released: String? = null,
    val thumbnail: String? = null
)

data class KitsuResponse(
    val data: List<KitsuData>? = null
)

data class KitsuData(
    val id: String,
    val attributes: KitsuAttributes? = null
)

data class KitsuAttributes(
    val canonicalTitle: String? = null,
    val slug: String? = null,
    val synopsis: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val averageRating: String? = null,
    val posterImage: KitsuImage? = null,
    val coverImage: KitsuImage? = null,
    val episodeCount: Int? = null,
    val episodeLength: Int? = null
)

data class KitsuImage(
    val original: String? = null,
    val large: String? = null,
    val medium: String? = null,
    val small: String? = null,
    val tiny: String? = null,
    val meta: KitsuImageMeta? = null
)

data class KitsuImageMeta(
    val dimensions: KitsuImageDimensions? = null
)

data class KitsuImageDimensions(
    val tiny: KitsuSize? = null,
    val small: KitsuSize? = null,
    val medium: KitsuSize? = null,
    val large: KitsuSize? = null,    val original: KitsuSize? = null
)

data class KitsuSize(
    val width: Int? = null,
    val height: Int? = null
)
