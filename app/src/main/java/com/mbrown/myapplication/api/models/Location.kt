package com.mbrown.myapplication.api.models

import java.net.URI

data class Location(val name: String, val url: URI) {

    val id get() = url.toString().split("/").last().toLong()
}