package com.kurostream.tv.ui.mylist

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
import androidx.navigation.NavController

@Composable
fun MyListScreen(navController: NavController) {
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "My Anime List",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(getMockWatchlist()) { anime ->
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
    anime: com.kurostream.tv.domain.model.Anime,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .focusable(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster
            anime.posterUrl?.let { posterUrl ->
                androidx.compose.foundation.Image(
                    painter = coil.compose.rememberAsyncImagePainter(posterUrl),
                    contentDescription = anime.title,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = "Progress: 0/12 episodes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }        }
    }
}

private fun getMockWatchlist(): List<com.kurostream.tv.domain.model.Anime> {
    return (1..10).map { i ->
        com.kurostream.tv.domain.model.Anime(
            id = "$i",
            title = "Watchlist Anime $i",
            episodes = emptyList(),
            posterUrl = "https://example.com/watchlist$i.jpg",
            rating = (50..100).random() / 10f
        )
    }
}
