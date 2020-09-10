package com.mbrown.myapplication

import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyService {
    companion object {
        val rmService: RickAndMortyService by lazy {
            create()
        }

        private fun create(): RickAndMortyService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava3CallAdapterFactory.create()
                )
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl("https://rickandmortyapi.com/api/")
                .build()

            return retrofit.create(RickAndMortyService::class.java)
        }
    }

    @GET("character")
    fun getCharacters() : Observable<Model.Response>

    @GET("location/{id}")
    fun getLocation(@Path("id") locationId: Long): Observable<Model.DetailedLocation>
}