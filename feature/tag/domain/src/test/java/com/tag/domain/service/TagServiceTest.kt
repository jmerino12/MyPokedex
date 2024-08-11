package com.tag.domain.service


import com.auth.domain.model.User
import com.auth.domain.repository.AuthRepository
import com.pokemon.domain.model.Pokemon
import com.tag.domain.exceptions.InvalidPokemonListException
import com.tag.domain.exceptions.TagAlreadyExistsException
import com.tag.domain.exceptions.TagNotFoundException
import com.tag.domain.model.Tag
import com.tag.domain.repository.TagRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.argThat
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class TagServiceTest {
    @Mock
    private lateinit var tagRepository: TagRepository

    @InjectMocks
    private lateinit var tagService: TagService


    @Test(expected = TagAlreadyExistsException::class)
    fun createTag_withEmptyPokemonList_throw_TagAlreadyExistsException() = runTest {
        //Arrange
        val tagName = "example"
        val pokemons = listOf<Pokemon>()

        whenever(tagRepository.getTagByName(tagName)).thenReturn(flow {
            emit(
                Tag(
                    name = tagName,
                    pokemons = listOf()
                )
            )
        })

        //Act
        tagService.createTag(tagName, pokemons)
    }

    @Test(expected = InvalidPokemonListException::class)
    fun createTag_withEmptyPokemonList_throw_InvalidPokemonListException() = runTest {
        //Arrange
        val tagName = "example"
        val pokemons = listOf<Pokemon>()

        whenever(tagRepository.getTagByName(tagName)).thenReturn(flow {
            emit(
                null
            )
        })

        //Act
        tagService.createTag(tagName, pokemons)
    }

    @Test
    fun createTag_withEmptyPokemonList_success() = runTest {
        //Arrange
        val tagName = "example"
        val pokemons = listOf(Pokemon("charmander", "", url = ""))
        val newTag = Tag(name = tagName, pokemons = pokemons)

        whenever(tagRepository.getTagByName(tagName)).thenReturn(flow {
            emit(
                null
            )
        })

        //Act
        tagService.createTag(tagName, pokemons)

        //Assert
        Mockito.verify(tagRepository).createTag(newTag)
    }

    @Test(expected = TagNotFoundException::class)
    fun updateTag_throws_TagNotFoundException() = runTest {
        val oldTagName = "example"
        val newTagName = "example"
        val pokemons = listOf(Pokemon("charmander", "", url = ""))


        whenever(tagRepository.getTagByName(oldTagName)).thenReturn(flow {
            emit(
                null
            )
        })

        //Act
        tagService.updateTag(oldTagName, newTagName, pokemons)
    }

    @Test(expected = TagAlreadyExistsException::class)
    fun updateTag_throws_TagAlreadyExistsException() = runTest {
        //Arrange
        val existingTag = Tag(name = "existing", pokemons = listOf())
        val newTag = Tag(name = "newTag", pokemons = listOf())
        val newName = "newTag"
        val newPokemons = listOf(Pokemon("Pikachu", "", url = ""))


        `when`(tagRepository.getTagByName("existing")).thenReturn(flowOf(existingTag))
        `when`(tagRepository.getTagByName(newName)).thenReturn(flowOf(newTag))

        //Act
        tagService.updateTag("existing", newName, newPokemons)


        //Assert

    }

    @Test
    fun updateTag_success() = runTest {
        // Arrange
        val existingTag = Tag(name = "existing", pokemons = listOf())
        val newName = "newTag"
        val newPokemons = listOf(Pokemon("Pikachu", "", url = ""))
        val user = User("uid", "email", "displayName")



        whenever(tagRepository.getTagByName("existing")).thenReturn(flowOf(existingTag))
        whenever(tagRepository.getTagByName(newName)).thenReturn(flowOf(null))

        // Act
        tagService.updateTag("existing", newName, newPokemons)

        // Assert
        val argumentCaptor = argumentCaptor<Tag>()
        verify(tagRepository).updateTag(argumentCaptor.capture())
        val capturedTag = argumentCaptor.firstValue

        assertEquals(newName, capturedTag.name)
        assertEquals(newPokemons, capturedTag.pokemons)
    }


    @Test
    fun addPokemonsToTag_success() = runTest {
        val existingTag = Tag(name = "existing", pokemons = listOf())
        val pokemons = listOf(Pokemon("charmander", "", url = ""))


        whenever(tagRepository.getTagByName(existingTag.name)).thenReturn(flow {
            emit(
                existingTag
            )
        })

        //Act
        tagService.addPokemonsToTag(tagName = existingTag.name, pokemons)

        verify(tagRepository).addPokemonToTag(
            eq(Tag(name = existingTag.name, pokemons = pokemons))
        )
    }

    @Test(expected = TagNotFoundException::class)
    fun addPokemonsToTag_throw() = runTest {
        val existingTag = Tag(name = "existing", pokemons = listOf())
        val pokemons = listOf(Pokemon("charmander", "", url = ""))


        whenever(tagRepository.getTagByName(existingTag.name)).thenReturn(flow {
            emit(
                null
            )
        })

        //Act
        tagService.addPokemonsToTag(tagName = existingTag.name, pokemons)

        //Assert
        verify(tagRepository, never()).addPokemonToTag(any())
    }


}