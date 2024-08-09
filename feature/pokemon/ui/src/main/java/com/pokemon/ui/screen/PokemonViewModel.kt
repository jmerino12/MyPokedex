package com.pokemon.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.IoDispatcher
import com.pokemon.domain.repositories.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) :
    ViewModel() {
    private val _uiState = MutableStateFlow<PokemonUiState>(PokemonUiState.LOADING)
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    fun getPokemons() {
        _uiState.value = PokemonUiState.LOADING
        viewModelScope.launch(ioDispatcher) {
            try {
                pokemonRepository.getPokemonList().collect {
                    if (it.isEmpty()) {
                        _uiState.value = PokemonUiState.EMPTY
                    } else {
                        _uiState.value = PokemonUiState.SUCCESS(it)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = PokemonUiState.ERROR
            }
        }
    }

}