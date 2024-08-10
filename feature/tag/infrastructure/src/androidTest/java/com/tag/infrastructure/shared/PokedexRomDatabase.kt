package com.tag.infrastructure.shared

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.core.database.shared.PokedexDatabase
import org.junit.After
import java.io.IOException

abstract class PokedexRomDatabase {
    protected lateinit var pokedexDatabase: PokedexDatabase

    open fun createDatabase() {
        val contexto = ApplicationProvider.getApplicationContext<Context>()
        pokedexDatabase =
            Room.inMemoryDatabaseBuilder(contexto, PokedexDatabase::class.java).build()
    }

    @After
    @Throws(IOException::class)
    open fun closeDatabase() {
        pokedexDatabase.close()
    }
}