package com.tag.infrastructure

import com.core.database.pokemon.dao.PokemonDao
import com.pokemon.infrastructure.anticorruption.PokemonTranslate
import com.tag.contracts.TagLocalRepository
import com.tag.domain.model.Tag
import com.tag.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TagProxyRepository @Inject constructor(
    private val tagLocalRepository: TagLocalRepository,
    private val pokemonDao: PokemonDao
) : TagRepository {
    override suspend fun createTag(tag: Tag) {
        val id = tagLocalRepository.insertTag(tag)
        val pokemons = tag.pokemons.map {
            PokemonTranslate.fromDomainToEntity(it, id)
        }
        pokemonDao.insertPokemons(pokemonEntities = pokemons)
    }

    override suspend fun deleteTag(tagName: String) {
        val tag = tagLocalRepository.getTagByName(tagName).firstOrNull()
        if (tag != null) {
            pokemonDao.deletePokemonsByTagId(tagId = tag.idTag)
            tagLocalRepository.deleteTagByName(tagName)
        }
    }

    override suspend fun updateTag(tag: Tag) {
        tagLocalRepository.update(tag)
    }

    override fun getTagByName(name: String): Flow<Tag?> {
        return tagLocalRepository.getTagByName(name)
    }

    override suspend fun addPokemonToTag(tag: Tag) {
        val myTag = tagLocalRepository.getTagByName(tag.name).firstOrNull()
        if (myTag != null) {
            val existingPokemonNames = tag.pokemons.map { it.name }.toSet()
            val newPokemons = tag.pokemons.filter { it.name !in existingPokemonNames }
            val newPokemonEntities = newPokemons.map {
                PokemonTranslate.fromDomainToEntity(it, myTag.idTag)
            }
            pokemonDao.insertPokemons(newPokemonEntities)
        }
    }
}