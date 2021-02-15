package com.example.newsapp

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.entity.Post

@Dao
interface PostDao {
    @Insert
    fun addPost(post: Post): Long

    @Update
    fun update(post: Post)

    @Delete
    fun delete(post: Post)

    @Query("SELECT * FROM post_table ORDER BY id ASC")
    fun getAllPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM post_table WHERE id = :id")
    fun getPost(id: Long): LiveData<Post>

}