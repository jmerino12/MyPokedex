package com.tag.domain.repository

import com.auth.domain.model.User
import com.tag.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    suspend fun createTag(tag: Tag, user: User?)
    suspend fun deleteTag(tagName: String, user: User?)
    suspend fun updateTag(tag: Tag, user: User?)
    fun getTagByName(name: String, user: User?): Flow<Tag?>
}