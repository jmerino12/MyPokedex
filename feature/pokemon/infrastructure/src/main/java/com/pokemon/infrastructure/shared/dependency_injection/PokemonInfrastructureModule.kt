package com.pokemon.infrastructure.shared.dependency_injection


import com.pokemon.domain.repositories.PokemonRepository
import com.pokemon.infrastructure.httpclient.service.PokemonService
import com.pokemon.infrastructure.repository.PokemonRetrofitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokemonInfrastructureModule {

    @Provides
    @Singleton
    fun providesPokemonServiceApi(retrofit: Retrofit): PokemonService {
        return retrofit.create(PokemonService::class.java)
    }


    @Provides
    @Singleton
    fun providesPokemonRepository(
        pokemonService: PokemonService
    ): PokemonRepository = PokemonRetrofitRepository(pokemonService)

}
