package com.kurostream.tv.ui.anilist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import javax.inject.Inject

@HiltViewModel
class AniListViewModel @Inject constructor(
    private val anilistSyncManager: com.kurostream.tv.data.remote.anilist.AniListSyncManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(AniListUiState())
    val uiState: StateFlow<AniListUiState> = _uiState.asStateFlow()

    init {
        fetchWatchlist()
    }

    fun fetchWatchlist() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val entries = anilistSyncManager.getWatchlist(getUserId())
            _uiState.value = _uiState.value.copy(
                entries = entries,
                isLoading = false
            )
        }
    }
    private fun getUserId(): Int {
        // Implementation to get user ID from token or cache
        return 0
    }

    fun updateProgress(mediaId: Int, progress: Int) {
        viewModelScope.launch {
            val success = anilistSyncManager.updateProgress(mediaId, progress)
            if (success) {
                fetchWatchlist() // Refresh the list
            }
        }
    }
}

data class AniListUiState(
    val entries: List<com.kurostream.tv.data.remote.anilist.AniListMediaEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@Composable
fun AniListTabScreen(
    viewModel: AniListViewModel = androidx.lifecycle.viewmodel.compose.hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Group entries by status                val groupedEntries = uiState.entries.groupBy { it.status }
                
                groupedEntries.forEach { (status, entries) ->
                    item {
                        Text(
                            text = status,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(entries) { entry ->
                        AniListEntryCard(
                            entry = entry,
                            onUpdateProgress = { progress ->
                                viewModel.updateProgress(entry.mediaId, progress)
                            }
                        )
                    }
                }
            }
        }

        // Refresh button
        FloatingActionButton(
            onClick = { viewModel.fetchWatchlist() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
        }
    }
}

@Composable
fun AniListEntryCard(
    entry: com.kurostream.tv.data.remote.anilist.AniListMediaEntry,
    onUpdateProgress: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .focusable()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster
            entry.media.coverImage?.extraLarge?.let { posterUrl ->
                AsyncImage(
                    model = posterUrl,
                    contentDescription = entry.media.title.english ?: entry.media.title.romaji,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = entry.media.title.english ?: entry.media.title.romaji ?: "Unknown",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Progress: ${entry.progress}/${entry.media.episodes ?: "??"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                LinearProgressIndicator(
                    progress = if (entry.media.episodes != null && entry.media.episodes > 0) {
                        entry.progress.toFloat() / entry.media.episodes
                    } else 0f,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Update button
            Button(
                onClick = {
                    val newProgress = if (entry.progress < (entry.media.episodes ?: Int.MAX_VALUE)) {                        entry.progress + 1
                    } else entry.progress
                    onUpdateProgress(newProgress)
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Mark +1")
            }
        }
    }
}
