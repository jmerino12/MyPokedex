package com.pokemon.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.IoDispatcher
import com.pokemon.domain.model.Pokemon
import com.pokemon.domain.repositories.PokemonRepository
import com.tag.domain.exceptions.TagException
import com.tag.domain.service.TagService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val tagService: TagService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) :
    ViewModel() {
    private val _uiState = MutableStateFlow<PokemonUiState>(PokemonUiState.LOADING)
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<Int?>(null)
    val errorMessage: StateFlow<Int?> = _errorMessage.asStateFlow()

    fun getPokemons() {
        _uiState.value = PokemonUiState.LOADING
        viewModelScope.launch(ioDispatcher) {
            try {
                pokemonRepository.getPokemonList().collect {
                    if (it.isEmpty()) {
                        _uiState.value = PokemonUiState.EMPTY
                    } else {
                        val pokemonsWithImages = it.map { pokemon ->
                            val imageUrl = pokemonRepository.getPokemonDetail(pokemon.url).first()
                            pokemon.copy(image = imageUrl.image)
                        }
                        _uiState.value = PokemonUiState.SUCCESS(pokemonsWithImages)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = PokemonUiState.ERROR(e.message)
            }
        }
    }

    fun createTagWithPokemons(tagName: String, pokemons: List<Pokemon>) {
        val currentState = _uiState.value
        viewModelScope.launch(ioDispatcher) {
            try {
                tagService.createTag(tagName, pokemons)
                _uiState.value = currentState
            } catch (e: TagException) {
                _errorMessage.value = e.messageResId
            }
        }
    }

}