package com.core.database.shared

import androidx.room.Database
import androidx.room.RoomDatabase
import com.core.database.pokemon.entity.PokemonEntity
import com.core.database.tag.dao.TagDao
import com.core.database.tag.entity.TagEntity

/**
 * Created by jmerino on 13/09/23
 */
@Database(
    entities = [PokemonEntity::class, TagEntity::class],
    version = 1
)
abstract class PokedexDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao

}