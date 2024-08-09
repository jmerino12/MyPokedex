package com.pokemon.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pokemon.domain.model.Pokemon

@Composable
fun PokemonScreen(
    modifier: Modifier = Modifier, pokemonUiState: PokemonUiState,
    getPokemons: () -> Unit,
) {
    LaunchedEffect(Unit) {
        getPokemons()
    }
    when {
        pokemonUiState is PokemonUiState.LOADING -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                )
            }

        }

        pokemonUiState is PokemonUiState.SUCCESS -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(pokemonUiState.pokemons) {
                    PokemonItem(pokemon = it)
                }
            }

        }

    }

}


@Composable
fun PokemonItem(pokemon: Pokemon) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = pokemon.name, style = MaterialTheme.typography.titleLarge)
    }

}

@Preview
@Composable
private fun PokemonScreenPreview() {
    PokemonScreen(
        pokemonUiState = PokemonUiState.LOADING,
        getPokemons = {}
    )
}


@Preview
@Composable
private fun PokemonItemPreview() {
    PokemonItem(Pokemon("Pikachú", null))
}
