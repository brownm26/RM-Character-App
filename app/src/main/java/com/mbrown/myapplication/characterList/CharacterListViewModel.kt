package com.mbrown.myapplication.characterList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbrown.myapplication.Model
import com.mbrown.myapplication.RickAndMortyService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class CharacterListViewModel : ViewModel() {
    private val _characters = MutableStateFlow<List<Model.Character>>(emptyList())
    val characters = _characters
        .onStart { fetchCharacters() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    var nextPage: Long? = 1
    var isLoading by mutableStateOf(false)

    private fun fetchCharacters() {
        viewModelScope.launch {
            if(isLoading) return@launch
            isLoading = true
            val page = nextPage
            runCatching {
                RickAndMortyService.rmService.getCharacters(page)
            }.onSuccess { result ->
                Timber.d(result.toString())
                if(page == null) {
                    _characters.value = emptyList()
                }
                _characters.value += result.results

                nextPage = result.info.nextPage
            }.onFailure {
                it.printStackTrace()
            }
            isLoading = false
        }
    }

    fun onScrolledToBottom() {
        if(!isLoading && nextPage != null) {
            fetchCharacters()
        }
    }
}