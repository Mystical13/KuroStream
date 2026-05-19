package com.kurostream.tv.ui.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DiscoverScreen(navController: NavController) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Discover Anime",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getMockAnimeList()) { anime ->
                AnimeGridItem(
                    anime = anime,
                    modifier = Modifier.focusRequester(focusRequester),
                    onClick = { navController.navigate("detail/${anime.id}") }
                )            }
        }
    }
}

@Composable
fun AnimeGridItem(
    anime: com.kurostream.tv.domain.model.Anime,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .fillMaxWidth()
            .focusable(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            anime.posterUrl?.let { posterUrl ->
                androidx.compose.foundation.Image(
                    painter = coil.compose.rememberAsyncImagePainter(posterUrl),
                    contentDescription = anime.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = anime.title,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 4.dp),
                maxLines = 1
            )
        }
    }
}

private fun getMockAnimeList(): List<com.kurostream.tv.domain.model.Anime> {
    return (1..30).map { i ->
        com.kurostream.tv.domain.model.Anime(
            id = "$i",
            title = "Anime Title $i",            episodes = emptyList(),
            posterUrl = "https://example.com/poster$i.jpg",
            rating = (50..100).random() / 10f
        )
    }
}
