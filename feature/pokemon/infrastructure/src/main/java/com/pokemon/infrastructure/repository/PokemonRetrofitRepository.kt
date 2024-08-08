package com.pokemon.infrastructure.repository

import com.pokemon.domain.model.Pokemon
import com.pokemon.domain.repositories.PokemonRepository
import com.pokemon.infrastructure.anticorruption.PokemonTranslate
import com.pokemon.infrastructure.httpclient.service.PokemonService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRetrofitRepository(private val pokemonService: PokemonService) : PokemonRepository {
    override fun getPokemonList(): Flow<List<Pokemon>> {
        return flow {
            emit(pokemonService.getPokemons().pokemons.map {
                PokemonTranslate.fromDtoToDomain(it)
            })
        }
    }
}