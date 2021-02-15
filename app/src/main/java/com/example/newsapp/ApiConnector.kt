package com.example.newsapp

import com.example.newsapp.entity.PostsResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiConnector {

    @GET("6125f2d0-0688-4547-aae8-0295d984f196")
    fun getPosts(): Call<PostsResponse>
}