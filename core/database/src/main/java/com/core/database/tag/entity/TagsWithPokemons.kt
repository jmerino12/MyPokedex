package com.core.database.tag.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.core.database.pokemon.entity.PokemonEntity

data class TagsWithPokemons(
    @Embedded val tag: TagEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tagId"
    )
    val tasks: List<PokemonEntity>?
)