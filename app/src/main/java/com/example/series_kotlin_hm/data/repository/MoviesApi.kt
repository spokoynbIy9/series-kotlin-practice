package com.example.series_kotlin_hm.data.repository

import com.example.series_kotlin_hm.data.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("/v1.4/movie")
    suspend fun getMovies(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("selectFields") selectFields: List<String> = MoviesResponse.getSelectFields()
    ): MoviesResponse
}
