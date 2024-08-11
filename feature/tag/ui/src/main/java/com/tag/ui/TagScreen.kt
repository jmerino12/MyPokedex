package com.tag.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pokemon.domain.model.Pokemon
import com.tag.domain.model.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun TagScreen(
    modifier: Modifier = Modifier,
    tagUiState: TagUiState,
    getTags: () -> Unit,
    goToPokemonScreen: () -> Unit,
    deleteTag: (String) -> Unit,
    logout: () -> Unit,

    ) {
    val composition by
    rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_state))
    LaunchedEffect(Unit) {
        getTags()
    }

    Scaffold(topBar = {
        if (tagUiState is TagUiState.EMPTY) {
            TopAppBar(
                title = { Text("Pokedex") },
                actions = {
                    Row {
                        IconButton(onClick = goToPokemonScreen) {
                            Icon(Icons.Default.AddCircle, contentDescription = "Add")
                        }

                        IconButton(onClick = logout) {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Close app"
                            )
                        }
                    }

                }
            )
        } else {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.my_tags_pokemons),
                    )
                },
                actions = {
                    Row {
                        IconButton(onClick = goToPokemonScreen) {
                            Icon(Icons.Default.AddCircle, contentDescription = "Add")
                        }

                        IconButton(onClick = logout) {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Close app"
                            )
                        }
                    }

                }
            )
        }
    }) {
        val arrangement = if (tagUiState is TagUiState.SUCCESS) {
            Arrangement.Top to Alignment.Start
        } else {
            Arrangement.Center to Alignment.CenterHorizontally
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = arrangement.first,
            horizontalAlignment = arrangement.second
        ) {
            when {
                tagUiState is TagUiState.LOADING -> {
                    CircularProgressIndicator()
                }

                tagUiState is TagUiState.SUCCESS -> {
                    LazyColumn {
                        items(tagUiState.tags) { tags ->
                            TagItem(tag = tags, deleteTag = deleteTag)
                        }
                    }
                }

                tagUiState is TagUiState.EMPTY -> {
                    LottieAnimation(
                        iterations = LottieConstants.IterateForever,
                        composition = composition,
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        text = stringResource(R.string.there_are_no_labels_you_can_create_your_own),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }


    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable

fun TagItem(modifier: Modifier = Modifier, tag: Tag, deleteTag: (String) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = { deleteTag(tag.name) },
            )

    ) {
        Text(text = tag.name, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(4.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(tag.pokemons) { pokemon ->
                ItemPokemon(pokemon)
            }
        }
    }
}

@Composable
fun ItemPokemon(pokemon: Pokemon) {
    Card(onClick = { /*TODO*/ }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(3.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.image)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = pokemon.name,
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = pokemon.name, style = MaterialTheme.typography.bodyLarge)
        }
    }


}

@Preview
@Composable
private fun TagItemPreview() {
    TagItem(
        deleteTag = {},
        tag = Tag(0L, "Prueba", listOf(Pokemon("Pikachu", "", "")))
    )
}


@Preview
@Composable
private fun TagScreenPreview() {
    TagScreen(
        tagUiState = TagUiState.SUCCESS(
            tags = listOf(
                Tag(
                    0L,
                    "Prueba",
                    listOf(Pokemon("Pikachu", "", ""))
                )
            )
        ), getTags = {}, goToPokemonScreen = {}, deleteTag = {}, logout = {})
}