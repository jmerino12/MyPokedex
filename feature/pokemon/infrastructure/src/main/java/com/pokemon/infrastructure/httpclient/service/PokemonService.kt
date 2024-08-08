package com.pokemon.infrastructure.httpclient.service

import com.pokemon.infrastructure.httpclient.dto.PokemonsDto
import retrofit2.http.GET

interface PokemonService {
    @GET("/pokemon?limit=50&offset=0")
    suspend fun getPokemons(): PokemonsDto
}