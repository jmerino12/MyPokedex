package com.tag.infrastructure

import com.core.database.tag.dao.TagDao
import com.tag.anticorruption.TagTranslate
import com.tag.contracts.TagLocalRepository
import com.tag.domain.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TagRoomRepository @Inject constructor(
    private val tagDao: TagDao,
) : TagLocalRepository {

    override fun insertTag(tag: Tag): Flow<Long> {
        return flow {
            val tagEntity = TagTranslate.fromDomainToEntityTag(tag)
            emit(tagDao.createTag(tagEntity))
        }
    }

    override suspend fun deleteTagByName(tagName: String) {
        return tagDao.deleteTagByName(tagName)
    }

    override suspend fun update(tag: Tag) {
        return tagDao.updateTag(
            TagTranslate.fromDomainToEntityTag(tag)
        )
    }

    override fun getTagByName(tagName: String): Flow<Tag?> {
        return tagDao.getTagByName(tagName).map {
            it?.let {
                TagTranslate.fromEntityTagToDomain(it)
            }
        }
    }
}