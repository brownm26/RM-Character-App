package com.mbrown.myapplication

import androidx.core.net.toUri
import java.net.URI

object Model {
    data class Response(val info: Info, val results: List<Character>)

    data class Info(val count: Int, val pages: Int, val next: String?, val prev: String?) {
        val nextPage get() = next?.toUri()?.getQueryParameter(PAGE_KEY)?.toLongOrNull()

        companion object {
            private const val PAGE_KEY = "page"
        }
    }

    data class Character(
        val id: Long,
        val name: String,
        val status: String,
        val species: String,
        val type: String,
        val gender: String,
        val origin: Location,
        val location: Location,
        val image: String,
        val episode: List<String>,
        val url: String,
        val created: String
    )

    data class Location(val name: String, val url: URI) {

        val id get() = url.toString().split("/").last().toLong()
    }

    data class DetailedLocation(
        val id: Long,
        val name: String,
        val type: String,
        val dimension: String,
        val residents: List<String>,
        val url: String,
        val created: String
    )

}