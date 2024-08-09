package com.pokemon.ui.screen

import com.pokemon.domain.model.Pokemon

sealed class PokemonUiState {
    data object LOADING : PokemonUiState()
    data class SUCCESS(val pokemons: List<Pokemon>) : PokemonUiState()
    data object EMPTY: PokemonUiState()
    data object ERROR : PokemonUiState()
}