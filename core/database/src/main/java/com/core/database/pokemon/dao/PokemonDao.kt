package com.core.database.pokemon.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.core.database.pokemon.entity.PokemonEntity
import com.core.database.tag.entity.TagEntity

@Dao
interface PokemonDao {
    @Transaction
    @Insert
    suspend fun insertPokemons(pokemonEntities: List<PokemonEntity>)

    @Transaction
    @Query("DELETE FROM pokemons WHERE tagId = :tagId")
    suspend fun deletePokemonsByTagId(tagId: Long)

    @Transaction
    @Delete
    suspend fun deletePokemons(pokemonEntity: PokemonEntity)
}