package com.tag.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.IoDispatcher
import com.pokemon.domain.repositories.PokemonRepository
import com.tag.domain.service.TagService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    private val tagService: TagService,
    private val pokemonRepository: PokemonRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TagUiState>(TagUiState.LOADING)
    val uiState: StateFlow<TagUiState> = _uiState.asStateFlow()

    fun getAllTags() {
        _uiState.value = TagUiState.LOADING
        viewModelScope.launch(ioDispatcher) {
            try {
                tagService.getAllTag().collect { tags ->
                    if (tags.isEmpty()) {
                        _uiState.value = TagUiState.EMPTY
                    } else {
                        val myTagsWithImages = tags.map { tag ->
                            val pokemonsWithImages = tag.pokemons.map { pokemon ->
                                val imageUrl = pokemonRepository.getPokemonDetail(pokemon.url).first()
                                pokemon.copy(image = imageUrl.image)
                            }
                            tag.copy(pokemons = pokemonsWithImages)
                        }
                        _uiState.value = TagUiState.SUCCESS(myTagsWithImages)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = TagUiState.ERROR
            }
        }
    }

}