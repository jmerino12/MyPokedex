package com.pokemon.infrastructure.anticorruption

import com.pokemon.domain.model.Pokemon
import com.pokemon.infrastructure.httpclient.dto.PokemonDto

class PokemonTranslate {
    companion object {
        fun fromDtoToDomain(pokemonDto: PokemonDto): Pokemon {
            return Pokemon(pokemonDto.name, pokemonDto.url)
        }
    }
}