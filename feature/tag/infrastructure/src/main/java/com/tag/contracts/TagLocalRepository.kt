package com.tag.contracts


import com.tag.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagLocalRepository {
    suspend fun insertTag(tag: Tag): Long
    suspend fun deleteTagByName(tagName: String)
    suspend fun update(tag: Tag)
    fun getTagByName(tagName: String): Flow<Tag?>
}