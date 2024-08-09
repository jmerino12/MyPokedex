package com.core.database.shared

import android.app.Application
import androidx.room.Room
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
}