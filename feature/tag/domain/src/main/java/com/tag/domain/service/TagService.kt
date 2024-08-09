package com.tag.domain.service

import com.auth.domain.repository.AuthRepository
import com.pokemon.domain.model.Pokemon
import com.tag.domain.R
import com.tag.domain.exceptions.InvalidPokemonListException
import com.tag.domain.exceptions.TagAlreadyExistsException
import com.tag.domain.exceptions.TagNotFoundException
import com.tag.domain.model.Tag
import com.tag.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class TagService(
    private val tagRepository: TagRepository,
    private val authRepository: AuthRepository
) {
    suspend fun createTag(tagName: String, pokemonList: List<Pokemon>) {
        val currentUser = authRepository.authState.first()

        val existingTag = tagRepository.getTagByName(tagName, currentUser).first()
        if (existingTag != null) {
            throw TagAlreadyExistsException(R.string.tag_already_exists)
        }
        if (pokemonList.isEmpty()) {
            throw InvalidPokemonListException(R.string.tag_there_must_be_at_least_one_pokemon)
        }
        val newTag = Tag(name = tagName, pokemons = pokemonList)

        tagRepository.createTag(newTag, currentUser)
    }

    suspend fun updateTag(name: String, newName: String, newPokemons: List<Pokemon>) {
        getTagByName(name)

        val currentUser = authRepository.authState.first()

        val tagWithNewName = tagRepository.getTagByName(newName, currentUser).first()
        if (tagWithNewName != null && tagWithNewName.name != name) {
            throw TagAlreadyExistsException(R.string.tag_already_exists)
        }

        tagRepository.updateTag(Tag(newName, newPokemons), currentUser)
    }

    suspend fun deleteTag(tagName: String) {
        val currentUser = authRepository.authState.first()

        tagRepository.getTagByName(tagName, currentUser).first()
            ?: throw TagNotFoundException(R.string.tag_already_not_found)

        tagRepository.deleteTag(tagName, currentUser)
    }

    suspend fun getTagByName(tagName: String): Flow<Tag?> {
        val currentUser = authRepository.authState.first()

        val existingTag = tagRepository.getTagByName(tagName, currentUser).first()
            ?: throw TagNotFoundException(R.string.tag_already_not_found)
        return flow { emit(existingTag) }
    }

}