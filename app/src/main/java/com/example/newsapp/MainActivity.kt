package com.example.newsapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.entity.Post
import com.example.newsapp.entity.PostsResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.news_dialog.view.*

class MainActivity : AppCompatActivity(), NewsAdapter.OnItemClickListener {
    private var posts = mutableListOf<Post>()
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)).get(NewsViewModel::class.java)
        setContentView(R.layout.activity_main)



        downloadData()
        getFromDB()
        showDialog()

    }





    private fun downloadData(){
        val isFirstUse: Boolean
        var settings: SharedPreferences = getSharedPreferences("PREF_SET_1", 0)
        isFirstUse = settings.getBoolean("FIRST_RUN", false)
        if(!isFirstUse) {
            newsViewModel.getNews()
            newsViewModel.getNewsResponseLiveData()?.observe(this, Observer<PostsResponse> {
                it.let {
                    posts.addAll(it.posts)
                    for(post in posts){
                        newsViewModel.addPost(post)
                        Log.d("DATABASE", "POST ADDED TO DB")
                    }
                    Log.d("DOWNLOADED", posts.toString())
                }
            })
            settings = getSharedPreferences("PREF_SET_1", 0)
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putBoolean("FIRST_RUN", true)
            editor.apply()

        }
    }

    private fun getFromDB(){
        newsViewModel.getAllPosts.observe(this, Observer<List<Post>>{
            newsAdapter = NewsAdapter(it.toTypedArray(), this, this)
            news_recycler_view.layoutManager = LinearLayoutManager(this)
            news_recycler_view.isNestedScrollingEnabled = false
            news_recycler_view.adapter = newsAdapter
        })
    }


    override fun onItemClick(position: Int) {
        val intent: Intent = Intent(this, NewsActivity::class.java).apply {
            putExtra("Post", newsAdapter.getPost(position).id.toLong())
        }
        startActivity(intent)
    }

    private fun showDialog(){
        add_news_button.setOnClickListener {
            val dialog = LayoutInflater.from(this).inflate(R.layout.news_dialog, null)
            val dialogBuilder = AlertDialog.Builder(this)
                .setView(dialog)
                .setTitle("Dodaj post")
            val alertDialog = dialogBuilder.show()
            dialog.dialog_apply_button.setOnClickListener{
                if(!dialog.dialog_title_edit_text?.text.toString().trim().isBlank() && !dialog.dialog_description_edit_text?.text.toString().trim().isBlank() &&
                        !dialog.dialog_image_url?.text.toString().trim().isBlank()){

                    addPost(dialog.dialog_title_edit_text?.text.toString(), dialog.dialog_description_edit_text?.text.toString(),
                        dialog.dialog_image_url?.text.toString() )


                    alertDialog.dismiss()
                }else{
                    Toast.makeText(this, "Nie można stworzyć posta", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dialog_cancel_button.setOnClickListener{
                alertDialog.dismiss()
            }

        }
    }

    private fun addPost(title: String, description: String, url: String){
        newsViewModel.addPost(Post(0, title, description, url)).observe(this, Observer<Long>{
            intent = Intent(this, NewsActivity::class.java).apply {
                putExtra("Post", it)
            }
            startActivity(intent)
        })
    }
}
