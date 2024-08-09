package com.core.database.tag.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.core.database.pokemon.entity.PokemonEntity
import com.core.database.tag.entity.TagEntity
import com.core.database.tag.entity.TagsWithPokemons

@Dao
interface TagDao {

    @Insert
    suspend fun createTag(tagEntity: TagEntity, pokemons: List<PokemonEntity>)
    @Delete
    suspend fun deleteTag(tagEntity: TagEntity)

    @Transaction
    @Query("SELECT * FROM tags")
    fun getAllWithPokemons(): List<TagsWithPokemons>

}