package com.mbrown.myapplication.presentation.characterDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.mbrown.myapplication.api.RickAndMortyService
import com.mbrown.myapplication.api.models.DetailedLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class CharacterDetailViewModel(val locationId: Long) : ViewModel() {

    private val _location = MutableStateFlow<DetailedLocation?>(null)
    val location = _location
        .onStart { fetchData() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
    var isLoading by mutableStateOf(false)

    private fun fetchData() {
        viewModelScope.launch {
            if(isLoading) return@launch
            isLoading = true
            runCatching {
                RickAndMortyService.rmService.getLocation(locationId)
            }.onSuccess { result ->
                Timber.d(result.toString())
                _location.value = result
            }.onFailure {
                it.printStackTrace()
            }
            isLoading = false
        }
    }

    companion object {
        fun factory(locationId: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return CharacterDetailViewModel(locationId) as T
                }
            }
    }
}