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
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class TagServiceTest {
    @Mock
    private lateinit var tagRepository: TagRepository

    @Mock
    private lateinit var authRepository: AuthRepository

    @InjectMocks
    private lateinit var tagService: TagService

    private val user = User("121", "jmerino1204@gmail.com", "jona")

    @Test(expected = TagAlreadyExistsException::class)
    fun createTag_withEmptyPokemonList_throw_TagAlreadyExistsException() = runTest {
        //Arrange
        val tagName = "example"
        val pokemons = listOf<Pokemon>()

        whenever(authRepository.authState).thenReturn(flow { emit(user) })
        whenever(tagRepository.getTagByName(tagName, user)).thenReturn(flow {
            emit(
                Tag(
                    tagName,
                    listOf()
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

        whenever(authRepository.authState).thenReturn(flow { emit(user) })
        whenever(tagRepository.getTagByName(tagName, user)).thenReturn(flow {
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
        val pokemons = listOf(Pokemon("charmander", ""))
        val newTag = Tag(name = tagName, pokemons = pokemons)

        whenever(authRepository.authState).thenReturn(flow { emit(user) })
        whenever(tagRepository.getTagByName(tagName, user)).thenReturn(flow {
            emit(
                null
            )
        })

        //Act
        tagService.createTag(tagName, pokemons)

        //Assert
        Mockito.verify(tagRepository).createTag(newTag, user)
    }

    @Test(expected = TagNotFoundException::class)
    fun updateTag_throws_TagNotFoundException() = runTest {
        val oldTagName = "example"
        val newTagName = "example"
        val pokemons = listOf(Pokemon("charmander", ""))

        whenever(authRepository.authState).thenReturn(flow { emit(user) })

        whenever(tagRepository.getTagByName(oldTagName, user)).thenReturn(flow {
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
        val existingTag = Tag("existing", listOf())
        val newTag = Tag("newTag", listOf())
        val newName = "newTag"
        val newPokemons = listOf(Pokemon("Pikachu", ""))
        whenever(authRepository.authState).thenReturn(flow { emit(user) })

        `when`(tagRepository.getTagByName("existing", user)).thenReturn(flowOf(existingTag))
        `when`(tagRepository.getTagByName(newName, user)).thenReturn(flowOf(newTag))

        //Act
        tagService.updateTag("existing", newName, newPokemons)


        //Assert

    }

    @Test
    fun updateTag_success() = runTest {
        // Arrange
        val existingTag = Tag("existing", listOf())
        val newName = "newTag"
        val newPokemons = listOf(Pokemon("Pikachu", ""))
        val user = User("uid", "email", "displayName")

        whenever(authRepository.authState).thenReturn(flow { emit(user) })


        whenever(tagRepository.getTagByName("existing", user)).thenReturn(flowOf(existingTag))
        whenever(tagRepository.getTagByName(newName, user)).thenReturn(flowOf(null))

        // Act
        tagService.updateTag("existing", newName, newPokemons)

        // Assert
        val argumentCaptor = argumentCaptor<Tag>()
        verify(tagRepository).updateTag(argumentCaptor.capture(), eq(user))
        val capturedTag = argumentCaptor.firstValue

        assertEquals(newName, capturedTag.name)
        assertEquals(newPokemons, capturedTag.pokemons)
    }



}