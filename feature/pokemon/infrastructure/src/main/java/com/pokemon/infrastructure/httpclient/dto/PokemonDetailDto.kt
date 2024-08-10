package com.pokemon.infrastructure.httpclient.dto

import com.google.gson.annotations.SerializedName

data class PokemonDetailDto(
    @SerializedName("sprites")
    val sprites: SpritesDto,
    @SerializedName("name")
    val name: String,
)

data class SpritesDto(
    @SerializedName("other")
    val other: OtherDto,
)

data class OtherDto(
    @SerializedName("dream_world")
    val dreamWorld: DreamWorldDto,
)

data class DreamWorldDto(
    @SerializedName("front_default")
    val frontDefault: String,
)