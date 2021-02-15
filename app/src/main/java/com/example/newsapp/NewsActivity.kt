package com.example.newsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.newsapp.entity.Post
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.edit_dialog.view.*

class NewsActivity: AppCompatActivity() {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var post:Post


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)).get(NewsViewModel::class.java)
        setContentView(R.layout.activity_news)
        val bundle: Bundle? = intent.extras

        var postId: Long
        bundle?.let {
            postId = it.getLong("Post")
            getPost(postId)

        }



    }


    private fun getPost(id: Long){
        newsViewModel.getPost(id).observe(this, Observer<Post>{
            post = it
            Glide
                .with(this)
                .load(post.icon)
                .into(news_activity_image)

            val count = "Liczba znaków w opisie: ${post.description.length}"
            news_activity_text.text = post.description
            news_activity_title.text = post.title
            news_activity_count.text = count

            updateTitle(post)
            updateDescription(post)
            updateImage(post)

        })
    }

    private fun openDialog(name: String, post: Post){
        val dialog = LayoutInflater.from(this).inflate(R.layout.edit_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialog)
            .setTitle("Edytuj post")
        val alertDialog = dialogBuilder.show()
        dialog.edit_dialog_field.hint = name
        dialog.edit_dialog_apply_button.setOnClickListener{
            if(name == "Tytuł") {

                if (dialog?.edit_dialog_field?.text.toString().trim().isNotBlank()) {
                    newsViewModel.updatePost(
                        Post(
                            post.id,
                            dialog?.edit_dialog_field?.text.toString(),
                            post.description,
                            post.icon
                        )
                    )
                    alertDialog.dismiss()
                    updateUI(post.id.toLong())
                } else {
                    Toast.makeText(this, "Nie można edytować posta", Toast.LENGTH_SHORT).show()
                }
            }else if(name == "Opis"){
                if (dialog?.edit_dialog_field?.text.toString().trim().isNotBlank()) {
                    newsViewModel.updatePost(
                        Post(
                            post.id,
                            post.title,
                            dialog?.edit_dialog_field?.text.toString(),
                            post.icon
                        )
                    )
                    alertDialog.dismiss()
                    updateUI(post.id.toLong())
                } else {
                    Toast.makeText(this, "Nie można edytować posta", Toast.LENGTH_SHORT).show()
                }
            }else{
                if (dialog?.edit_dialog_field?.text.toString().trim().isNotBlank()) {
                    newsViewModel.updatePost(
                        Post(
                            post.id,
                            post.title,
                            post.description,
                            dialog?.edit_dialog_field?.text.toString()
                        )
                    )
                    alertDialog.dismiss()
                    updateUI(post.id.toLong())
                } else {
                    Toast.makeText(this, "Nie można edytować posta", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.edit_dialog_cancel_button.setOnClickListener{
            alertDialog.dismiss()
        }


    }

    private fun updateTitle(post: Post){
        news_activity_title.setOnLongClickListener{
            openDialog("Tytuł", post)
            true
        }
    }

    private fun updateDescription(post: Post){
        news_activity_text.setOnLongClickListener{
            openDialog("Opis", post)
            true
        }
    }

    private fun updateImage(post: Post){
        news_activity_image.setOnLongClickListener{
            openDialog("URL zdjęcia", post)
            true
        }
    }

    private fun updateUI(id: Long){
        finish()
        intent.putExtra("Post", id)
        startActivity(intent)
    }




}