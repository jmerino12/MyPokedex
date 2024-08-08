package com.pokemon.infrastructure.httpclient.dto

import com.google.gson.annotations.SerializedName

data class PokemonsDto(
    @SerializedName("results")
    val pokemons: List<PokemonDto>
)
