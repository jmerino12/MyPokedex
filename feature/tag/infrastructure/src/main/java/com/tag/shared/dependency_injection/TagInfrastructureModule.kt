package com.tag.shared.dependency_injection


import com.core.database.pokemon.dao.PokemonDao
import com.core.database.tag.dao.TagDao
import com.tag.contracts.TagLocalRepository
import com.tag.domain.repository.TagRepository
import com.tag.infrastructure.TagProxyRepository
import com.tag.infrastructure.TagRoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TagInfrastructureModule {

    @Provides
    @Singleton
    fun provideTagRepository(
        tagLocalRepository: TagLocalRepository,
        pokemonDao: PokemonDao
    ): TagRepository = TagProxyRepository(tagLocalRepository, pokemonDao)

    @Provides
    @Singleton
    fun provideTagLocalRepository(tagDao: TagDao): TagLocalRepository = TagRoomRepository(tagDao)

}


