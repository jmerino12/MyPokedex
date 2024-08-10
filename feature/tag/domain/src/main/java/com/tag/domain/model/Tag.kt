package com.tag.domain.model

import com.pokemon.domain.model.Pokemon

data class Tag(
    val idTag: Long = -1,
    val name: String,
    val pokemons: List<Pokemon> = emptyList()
)