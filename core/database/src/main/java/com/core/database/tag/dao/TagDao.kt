package com.core.database.tag.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.core.database.pokemon.dao.PokemonDao
import com.core.database.pokemon.entity.PokemonEntity
import com.core.database.tag.entity.TagEntity
import com.core.database.tag.entity.TagsWithPokemons
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface TagDao {
    @Insert
    suspend fun createTag(tagEntity: TagEntity): Long

    @Transaction
    @Query("SELECT * FROM tags")
    fun getAllWithPokemons(): Flow<List<TagsWithPokemons>>

    @Transaction
    @Query("SELECT * FROM tags WHERE name = :tagName")
    fun getTagByName(tagName: String): Flow<TagsWithPokemons?>

    @Transaction
    @Query("DELETE FROM TAGS WHERE name = :tagName")
    suspend fun deleteTagByName(tagName: String)

    @Transaction
    @Update
    suspend fun updateTag(tagEntity: TagEntity)
}