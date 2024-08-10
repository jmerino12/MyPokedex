package com.tag.domain.repository

import com.tag.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    suspend fun createTag(tag: Tag)
    suspend fun deleteTag(tagName: String)
    suspend fun updateTag(tag: Tag)
    fun getTagByName(name: String): Flow<Tag?>
    suspend fun addPokemonToTag(tag: Tag)
}