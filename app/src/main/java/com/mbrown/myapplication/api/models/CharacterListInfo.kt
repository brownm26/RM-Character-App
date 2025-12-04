package com.mbrown.myapplication.api.models

import androidx.core.net.toUri

data class CharacterListInfo(val count: Int, val pages: Int, val next: String?, val prev: String?) {
    val nextPage get() = next?.toUri()?.getQueryParameter(PAGE_KEY)?.toLongOrNull()

    companion object {
        private const val PAGE_KEY = "page"
    }
}