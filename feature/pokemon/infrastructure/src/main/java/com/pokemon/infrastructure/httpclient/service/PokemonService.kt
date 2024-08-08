package com.pokemon.infrastructure.httpclient.service

import com.pokemon.infrastructure.httpclient.dto.PokemonDetailDto
import com.pokemon.infrastructure.httpclient.dto.PokemonsDto
import retrofit2.http.GET
import retrofit2.http.Url

interface PokemonService {
    @GET("/pokemon?limit=50&offset=0")
    suspend fun getPokemons(): PokemonsDto

    @GET
    suspend fun getPokemonInfo(@Url url: String): PokemonDetailDto
}