package com.tag.infrastructure

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.core.database.pokemon.dao.PokemonDao
import com.core.database.tag.dao.TagDao
import com.core.database.tag.entity.TagEntity
import com.pokemon.domain.model.Pokemon
import com.tag.contracts.TagLocalRepository
import com.tag.domain.model.Tag
import com.tag.infrastructure.shared.PokedexRomDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(AndroidJUnit4::class)
class TagProxyRepositoryTest : PokedexRomDatabase() {

    private lateinit var pokemonDao: PokemonDao
    private lateinit var tagDao: TagDao

    private lateinit var tagLocalRepository: TagLocalRepository

    private lateinit var tagProxyRepository: TagProxyRepository

    @Before
    override fun createDatabase() {
        super.createDatabase()
        pokemonDao = pokedexDatabase.pokemonDao()
        tagDao = pokedexDatabase.tagDao()
    }

    @After
    override fun closeDatabase() {
        super.closeDatabase()
    }


    @Test
    fun createTag_with_only_one_pokemon() = runTest {
        //Arrange
        tagLocalRepository = Mockito.mock(TagLocalRepository::class.java)
        tagProxyRepository = TagProxyRepository(tagLocalRepository, pokemonDao)
        val tagName = "tagName"
        val pokemonLists = listOf(Pokemon("Pikachu", null))
        val tag = Tag(name = tagName, pokemons = pokemonLists)
        Mockito.`when`(tagLocalRepository.insertTag(tag)).thenReturn(flow { emit(1L) })
        Mockito.`when`(tagLocalRepository.getTagByName(tagName))
            .thenReturn(flow { emit(Tag(1L, tagName, pokemonLists)) })

        //Act
        tagProxyRepository.createTag(tag)

        val result = tagProxyRepository.getTagByName(tagName).first()

        //Assert
        Assert.assertEquals(1, result?.pokemons?.size)
    }

    @Test
    fun createTag_empty_pokemon() = runTest {
        //Arrange
        tagLocalRepository = Mockito.mock(TagLocalRepository::class.java)
        tagProxyRepository = TagProxyRepository(tagLocalRepository, pokemonDao)
        val tagName = "tagName"
        val pokemonLists = listOf<Pokemon>()
        val tag = Tag(name = tagName, pokemons = pokemonLists)
        Mockito.`when`(tagLocalRepository.insertTag(tag)).thenReturn(flow { emit(1L) })
        Mockito.`when`(tagLocalRepository.getTagByName(tagName))
            .thenReturn(flow { emit(Tag(1L, tagName, pokemonLists)) })

        //Act
        tagProxyRepository.createTag(tag)

        val result = tagProxyRepository.getTagByName(tagName).first()

        //Assert
        Assert.assertEquals(0, result?.pokemons?.size)
    }

    @Test
    fun deleteTagWithPokemons_success() = runTest {
        //Arrange
        tagLocalRepository = Mockito.mock(TagLocalRepository::class.java)
        tagProxyRepository = TagProxyRepository(tagLocalRepository, pokemonDao)
        val tagName = "tagName"
        val pokemonLists = listOf(Pokemon("Pikachu", null), Pokemon("Charmander", null))

        val tag = Tag(name = tagName, pokemons = pokemonLists)
        Mockito.`when`(tagLocalRepository.insertTag(tag)).thenReturn(flow { emit(1L) })
        Mockito.`when`(tagLocalRepository.getTagByName(tagName))
            .thenReturn(flow { emit(Tag(1L, tagName, pokemonLists)) })
        tagProxyRepository.createTag(tag)

        //Act
        tagProxyRepository.deleteTag(tagName)

        //Assert
        Mockito.verify(tagLocalRepository).deleteTagByName(tagName)
    }

    @Test
    fun deleteTagWithPokemons_real_implementation() = runTest {
        // Arrange
        tagLocalRepository = TagRoomRepository(tagDao)
        tagProxyRepository = TagProxyRepository(tagLocalRepository, pokemonDao)
        val tagName = "tagName"
        val pokemonLists = listOf(Pokemon("Pikachu", null), Pokemon("Charmander", null))

        val tag = Tag(name = tagName, pokemons = pokemonLists)


        // Ejecutar la creación del tag
        tagProxyRepository.createTag(tag)

        // Act
        tagProxyRepository.deleteTag(tagName)

        // Recolectar el flujo y verificar si el tag fue eliminado
        val result = tagProxyRepository.getTagByName(tagName).firstOrNull()

        // Assert
        Assert.assertNull(result) // Verifica que el tag ha sido eliminado
    }

    @Test
    fun deleteTagWithPokemons() = runTest {
        // Arrange
        tagLocalRepository = Mockito.mock(TagLocalRepository::class.java)
        tagProxyRepository = TagProxyRepository(tagLocalRepository, pokemonDao)

        val tagName = "tagName"
        val pokemonLists = listOf(Pokemon("Pikachu", null), Pokemon("Charmander", null))
        val tag = Tag(name = tagName, pokemons = pokemonLists)

        Mockito.`when`(tagLocalRepository.getTagByName(tagName))
            .thenReturn(flowOf(tag))
            .thenReturn(flowOf(null))  // Simula que el tag ha sido eliminado

        // Act
        tagProxyRepository.createTag(tag)
        tagProxyRepository.deleteTag(tagName)
        val result = tagProxyRepository.getTagByName(tagName).firstOrNull()

        // Assert
        Assert.assertNull(result)
    }


}