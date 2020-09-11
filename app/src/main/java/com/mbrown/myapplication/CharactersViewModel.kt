package com.mbrown.myapplication

import androidx.lifecycle.ViewModel

class CharactersViewModel : ViewModel() {
    val characters = mutableMapOf<Long, Model.Character>()
    var nextPage: Long? = 1
    var loading: Boolean = false
}