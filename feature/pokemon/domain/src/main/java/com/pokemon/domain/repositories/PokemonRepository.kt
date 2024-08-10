package com.pokemon.domain.repositories

import com.pokemon.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<List<Pokemon>>
    fun getPokemonDetail(url: String): Flow<Pokemon>
}