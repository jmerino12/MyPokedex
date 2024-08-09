package com.tag.domain.model

import com.pokemon.domain.model.Pokemon

data class Tag(
    val name: String,
    val pokemons: List<Pokemon>
)