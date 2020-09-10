package com.mbrown.myapplication

import androidx.lifecycle.ViewModel

class CharactersViewModel : ViewModel() {
    val characters = mutableMapOf<Long, Model.Character>()
}