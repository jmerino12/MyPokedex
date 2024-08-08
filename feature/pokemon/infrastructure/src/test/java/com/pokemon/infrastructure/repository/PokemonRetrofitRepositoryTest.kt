package com.pokemon.infrastructure.repository

import com.pokemon.infrastructure.httpclient.dto.PokemonDto
import com.pokemon.infrastructure.httpclient.dto.PokemonsDto
import com.pokemon.infrastructure.httpclient.service.PokemonService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class PokemonRetrofitRepositoryTest {

    @Mock
    private lateinit var pokemonService: PokemonService

    @InjectMocks
    private lateinit var pokemonRetrofitRepository: PokemonRetrofitRepository

    @Test
    fun getPokemonsList_empty() = runTest {
        //Arrange
        val pokemonsList = listOf<PokemonDto>()
        val pokemonsDto = PokemonsDto(pokemonsList)

        whenever(pokemonService.getPokemons()).thenReturn(pokemonsDto)
        //Act
        val result = pokemonRetrofitRepository.getPokemonList().first()

        //Assert
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun getPokemonsList_noEmpty() = runTest {
        //Arrange
        val pokemonsList = listOf(PokemonDto("charmander", ""))
        val pokemonsDto = PokemonsDto(pokemonsList)

        whenever(pokemonService.getPokemons()).thenReturn(pokemonsDto)
        //Act
        val result = pokemonRetrofitRepository.getPokemonList().first()

        //Assert
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test
    fun getPokemonList_with_404_error() = runTest {
        //Arrange
        val response = Response.error<PokemonsDto>(404, "Not Found".toResponseBody(null))
        whenever(pokemonService.getPokemons()).thenThrow(HttpException(response))

        try {
            //Act
            pokemonRetrofitRepository.getPokemonList().collect {}
        } catch (e: HttpException) {
            //Assert
            assertEquals(404, e.code())
        }
    }

    @Test
    fun `test getPokemonList with 200 success`() = runTest {
        //Arrange
        val pokemonList = listOf(
            PokemonDto("charmander", "https://pokeapi.co/api/v2/pokemon/4/")
        )
        val responseDto = PokemonsDto(pokemonList)

        whenever(pokemonService.getPokemons()).thenReturn(responseDto)

        // Act
        val result = pokemonRetrofitRepository.getPokemonList().toList()

        // Assert
        assertEquals(1, result[0].size)
        assertEquals("charmander", result[0][0].name)
        assertEquals("https://pokeapi.co/api/v2/pokemon/4/", result[0][0].image)
    }

    @Test
    fun getPokemonList_with_500_error() = runTest {
        //Arrange
        val response =
            Response.error<PokemonsDto>(500, "Internal Server Error".toResponseBody(null))
        whenever(pokemonService.getPokemons()).thenThrow(HttpException(response))

        try {
            //Act
            pokemonRetrofitRepository.getPokemonList().collect {}
        } catch (e: HttpException) {
            //Assert
            assertEquals(500, e.code())
        }
    }
}