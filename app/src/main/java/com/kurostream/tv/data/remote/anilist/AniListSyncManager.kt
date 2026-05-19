package com.kurostream.tv.data.remote.anilist

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType

@Singleton
class AniListSyncManager @Inject constructor(
    private val context: Context,
    private val dataStore: DataStore<Preferences>,
    private val okHttpClient: OkHttpClient
) {
    companion object {
        private const val CLIENT_ID = "YOUR_ANILIST_CLIENT_ID"
        private const val REDIRECT_URI = "kurostream://anilist/callback"
        private const val API_URL = "https://graphql.anilist.co"
    }

    private val accessTokenKey = stringPreferencesKey("anilist_access_token")
    private val refreshTokenKey = stringPreferencesKey("anilist_refresh_token")

    suspend fun getAuthUrl(): String {
        return "https://anilist.co/api/v2/oauth/authorize?" +
                "client_id=$CLIENT_ID&" +
                "redirect_uri=$REDIRECT_URI&" +
                "response_type=code"
    }

    suspend fun exchangeCodeForToken(code: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val body = """
                {
                    "grant_type": "authorization_code",
                    "client_id": "$CLIENT_ID",
                    "client_secret": "YOUR_CLIENT_SECRET",
                    "code": "$code",
                    "redirect_uri": "$REDIRECT_URI"
                }            """.trimIndent().toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("https://anilist.co/api/v2/oauth/token")
                .post(body)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                // Parse response and save tokens
                // Implementation depends on response format
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAccessToken(): String? {
        return dataStore.data.first()[accessTokenKey]
    }

    suspend fun updateProgress(
        mediaId: Int,
        progress: Int,
        status: String = "CURRENT"
    ): Boolean = withContext(Dispatchers.IO) {
        val token = getAccessToken() ?: return@withContext false

        val mutation = """
            mutation {
                SaveMediaListEntry(
                    mediaId: $mediaId
                    progress: $progress
                    status: $status
                ) {
                    id
                    progress
                    status
                    media {
                        id
                        title {
                            romaji
                            english
                        }
                    }
                }            }
        """.trimIndent()

        try {
            val body = """
                {
                    "query": "${mutation.replace("\n", "\\n").replace("\"", "\\\"")}"
                }
            """.trimIndent().toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build()

            val response = okHttpClient.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getWatchlist(userId: Int): List<AniListMediaEntry> = withContext(Dispatchers.IO) {
        val query = """
            query {
                MediaListCollection(
                    userId: $userId
                    type: ANIME
                    status_not_in: [PLANNING]
                ) {
                    lists {
                        name
                        entries {
                            id
                            status
                            progress
                            media {
                                id
                                title {
                                    romaji
                                    english
                                }
                                coverImage {
                                    extraLarge
                                    large
                                }
                                episodes
                                averageScore                                startDate {
                                    year
                                    month
                                    day
                                }
                            }
                        }
                    }
                }
            }
        """.trimIndent()

        try {
            val body = """
                {
                    "query": "${query.replace("\n", "\\n").replace("\"", "\\\"")}"
                }
            """.trimIndent().toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url(API_URL)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                // Parse response and return list of entries
                // Implementation depends on JSON parsing
                emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

data class AniListMediaEntry(
    val id: Int,
    val mediaId: Int,
    val status: String,
    val progress: Int,
    val media: AniListMedia
)

data class AniListMedia(
    val id: Int,
    val title: AniListTitle,    val coverImage: AniListCoverImage?,
    val episodes: Int?,
    val averageScore: Float?,
    val startDate: AniListDate?
)

data class AniListTitle(
    val romaji: String?,
    val english: String?
)

data class AniListCoverImage(
    val extraLarge: String?,
    val large: String?
)

data class AniListDate(
    val year: Int?,
    val month: Int?,
    val day: Int?
)
