package com.kurostream.tv.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.kurostream.tv.domain.model.Anime

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Featured Anime",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(uiState.featuredAnime) { anime ->
                AnimeItem(
                    anime = anime,
                    modifier = Modifier.focusRequester(focusRequester),
                    onClick = { navController.navigate("detail/${anime.id}") }
                )
            }
            item {
                Text(
                    text = "Popular This Week",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(uiState.popularAnime) { anime ->
                AnimeItem(
                    anime = anime,
                    modifier = Modifier.focusRequester(focusRequester),
                    onClick = { navController.navigate("detail/${anime.id}") }
                )
            }
        }
    }
}

@Composable
fun AnimeItem(
    anime: Anime,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .focusable(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster
            anime.posterUrl?.let { posterUrl ->
                androidx.compose.foundation.Image(
                    painter = coil.compose.rememberAsyncImagePainter(posterUrl),
                    contentDescription = anime.title,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                anime.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                anime.rating?.let { rating ->
                    LinearProgressIndicator(
                        progress = rating / 10f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@androidx.lifecycle.ViewModel
class HomeViewModel @javax.inject.Inject constructor(
    private val animeRepository: com.kurostream.tv.domain.repository.AnimeRepository
) : androidx.lifecycle.ViewModel() {
    
    private val _uiState = androidx.lifecycle.mutableStateOf(HomeUiState())
    val uiState: androidx.lifecycle.State<HomeUiState> = _uiState

    init {
        loadFeaturedAnime()
        loadPopularAnime()
    }

    private fun loadFeaturedAnime() {
        viewModelScope.launch {            animeRepository.getFeaturedAnime().collect { animeList ->
                _uiState.value = _uiState.value.copy(featuredAnime = animeList)
            }
        }
    }

    private fun loadPopularAnime() {
        viewModelScope.launch {
            // Mock data for now
            val mockAnime = listOf(
                Anime(
                    id = "1",
                    title = "Demon Slayer",
                    episodes = emptyList(),
                    posterUrl = "https://example.com/demo.jpg",
                    rating = 9.2f
                ),
                Anime(
                    id = "2",
                    title = "Jujutsu Kaisen",
                    episodes = emptyList(),
                    posterUrl = "https://example.com/jujutsu.jpg",
                    rating = 8.8f
                )
            )
            _uiState.value = _uiState.value.copy(popularAnime = mockAnime)
        }
    }
}

data class HomeUiState(
    val featuredAnime: List<Anime> = emptyList(),
    val popularAnime: List<Anime> = emptyList()
)
