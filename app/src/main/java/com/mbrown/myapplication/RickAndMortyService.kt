package com.mbrown.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyService {
    companion object {
        val rmService: RickAndMortyService by lazy {
            create()
        }

        private fun create(): RickAndMortyService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl("https://rickandmortyapi.com/api/")
                .build()

            return retrofit.create(RickAndMortyService::class.java)
        }
    }

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Long? = null) : Model.Response

    @GET("location/{id}")
    suspend fun getLocation(@Path("id") locationId: Long): Model.DetailedLocation
}