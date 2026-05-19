package com.kurostream.tv.data.local.profile

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) {
    private val profilesKey = stringPreferencesKey("user_profiles")
    private val activeProfileKey = stringPreferencesKey("active_profile_id")
    private val profileNamesKey = stringSetPreferencesKey("profile_names")

    suspend fun createProfile(name: String, pin: String? = null): Boolean {
        val profileId = generateProfileId()
        val profile = UserProfile(
            id = profileId,
            name = name,
            pinHash = pin?.let { hashPin(it) },
            createdAt = System.currentTimeMillis()
        )

        val profiles = getAllProfiles().toMutableList()
        profiles.add(profile)

        dataStore.edit { preferences ->
            preferences[profilesKey] = profiles.toJson()
            preferences[activeProfileKey] = profileId
            val names = preferences[profileNamesKey]?.toMutableSet() ?: mutableSetOf()
            names.add(name)
            preferences[profileNamesKey] = names
        }

        return true
    }

    suspend fun getAllProfiles(): List<UserProfile> {
        val json = dataStore.data.value[profilesKey] ?: return emptyList()
        return json.fromJson()
    }
    suspend fun getActiveProfile(): UserProfile? {
        val activeId = dataStore.data.value[activeProfileKey] ?: return null
        return getAllProfiles().find { it.id == activeId }
    }

    suspend fun setActiveProfile(profileId: String): Boolean {
        dataStore.edit { preferences ->
            preferences[activeProfileKey] = profileId
        }
        return true
    }

    suspend fun verifyPin(profileId: String, pin: String): Boolean {
        val profiles = getAllProfiles()
        val profile = profiles.find { it.id == profileId } ?: return false
        return profile.pinHash?.let { it == hashPin(pin) } ?: true
    }

    suspend fun updateProfile(profile: UserProfile): Boolean {
        val profiles = getAllProfiles().toMutableList()
        val index = profiles.indexOfFirst { it.id == profile.id }
        if (index != -1) {
            profiles[index] = profile
            dataStore.edit { preferences ->
                preferences[profilesKey] = profiles.toJson()
            }
        }
        return true
    }

    suspend fun deleteProfile(profileId: String): Boolean {
        val profiles = getAllProfiles().toMutableList()
        profiles.removeAll { it.id == profileId }
        
        dataStore.edit { preferences ->
            preferences[profilesKey] = profiles.toJson()
            if (preferences[activeProfileKey] == profileId && profiles.isNotEmpty()) {
                preferences[activeProfileKey] = profiles.first().id
            } else if (profiles.isEmpty()) {
                preferences.remove(activeProfileKey)
            }
        }
        return true
    }

    private fun generateProfileId(): String {
        return "profile_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }
    private fun hashPin(pin: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(pin.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

    private fun List<UserProfile>.toJson(): String {
        // Implementation using Moshi/Gson
        return ""
    }

    private fun String.fromJson(): List<UserProfile> {
        // Implementation using Moshi/Gson
        return emptyList()
    }
}

data class UserProfile(
    val id: String,
    val name: String,
    val pinHash: String? = null,
    val isKidsMode: Boolean = false,
    val createdAt: Long,
    val settings: ProfileSettings = ProfileSettings()
)

data class ProfileSettings(
    val preferredLanguage: String = "en",
    val preferredSubtitleLanguage: String = "en",
    val preferredQuality: String = "720p",
    val autoPlayNext: Boolean = true,
    val skipIntro: Boolean = true,
    val skipOutro: Boolean = true,
    val autoScrobble: Boolean = true,
    val theme: String = "dark"
)
