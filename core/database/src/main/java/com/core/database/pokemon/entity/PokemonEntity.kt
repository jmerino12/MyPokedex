package com.core.database.pokemon.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemons")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    var tagId: Long
)