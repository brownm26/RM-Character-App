package com.mbrown.myapplication.api.models

data class DetailedLocation(
    val id: Long,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
)