package com.core.database.shared

import android.app.Application
import androidx.room.Room
import com.core.database.pokemon.dao.PokemonDao
import com.core.database.tag.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun databaseProvider(app: Application): PokedexDatabase =
        Room.databaseBuilder(app, PokedexDatabase::class.java, "pokedex-database")
            .build()


    @Provides
    fun providesTagDao(database: PokedexDatabase): TagDao = database.tagDao()

    @Provides
    fun providesPokemonDao(database: PokedexDatabase): PokemonDao = database.pokemonDao()
}