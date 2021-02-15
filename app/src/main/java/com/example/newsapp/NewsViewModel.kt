package com.example.newsapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.entity.Post
import com.example.newsapp.entity.PostsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    private val newsRepository: NewsRepository = NewsRepository(application)
    private val newsResponseLiveData: LiveData<PostsResponse>?
    val getAllPosts: LiveData<List<Post>> = newsRepository.getAllPosts


    init {
        newsResponseLiveData = newsRepository.getNewsResponseLiveData()
    }

    fun getNews(){
        newsRepository.getNews()
    }

    fun getNewsResponseLiveData():LiveData<PostsResponse>?{
        return newsResponseLiveData
    }

    fun addPost(post : Post): LiveData<Long>{
        val liveData = MutableLiveData<Long>()
        viewModelScope.launch(Dispatchers.IO){
            liveData.postValue(newsRepository.addPost(post))
        }
        return liveData
    }

    fun updatePost(post: Post){
        viewModelScope.launch(Dispatchers.IO){
            newsRepository.updatePost(post)
        }
    }

    fun getPost(id: Long): LiveData<Post> = newsRepository.getPost(id)
}