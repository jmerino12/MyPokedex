package com.pokemon.infrastructure.anticorruption

import com.core.database.pokemon.entity.PokemonEntity
import com.pokemon.domain.model.Pokemon
import com.pokemon.infrastructure.httpclient.dto.PokemonDto

class PokemonTranslate {
    companion object {
        fun fromDtoToDomain(pokemonDto: PokemonDto): Pokemon {
            return Pokemon(pokemonDto.name, pokemonDto.url)
        }

        fun fromDomainToEntity(pokemon: Pokemon, tagId: Long): PokemonEntity {
            return PokemonEntity(0, pokemon.name, tagId)
        }

        fun fromEntityToDomain(pokemonEntity: PokemonEntity): Pokemon {
            return Pokemon(pokemonEntity.name, "url")
        }
    }
}