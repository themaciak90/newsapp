package com.example.newsapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.entity.Post
import com.example.newsapp.entity.PostsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRepository (application: Application) {
    companion object{
        const val BASE_URL = "https://run.mocky.io/v3/"
    }
    private val apiConnector: ApiConnector
    private val newsResponseMutableLiveData: MutableLiveData<PostsResponse> = MutableLiveData()
    private val postDatabase: PostDatabase = PostDatabase.getDatabase(application)
    private val postDao: PostDao = postDatabase.getPostDao()
    val getAllPosts: LiveData<List<Post>> = postDao.getAllPosts()

    init{
        apiConnector = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiConnector::class.java)

    }

    fun getNews(){
        apiConnector.getPosts().enqueue(object: Callback<PostsResponse>{
            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                if(response.isSuccessful){
                    newsResponseMutableLiveData.value = response.body()
                    Log.d("SUCCESS", "DOWNLOADED DATA")
                }
            }

            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                Log.d("FAILURE", "CANNOT DOWNLOAD DATA " + t.message)
            }
        })
    }

    fun getNewsResponseLiveData():LiveData<PostsResponse>?{
        return newsResponseMutableLiveData
    }

     fun addPost(post: Post): Long{
        return postDao.addPost(post)
    }

    fun getPost(id: Long): LiveData<Post> = postDao.getPost(id)

    fun updatePost(post: Post){
        postDao.update(post)
    }

}