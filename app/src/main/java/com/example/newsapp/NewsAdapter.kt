package com.example.newsapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.entity.Post
import java.net.URL

class NewsAdapter (private val news: Array<Post>, private val context: Context, private val listener: OnItemClickListener): RecyclerView.Adapter<NewsAdapter.NewsHolder>(){



    inner class NewsHolder(view: View) :RecyclerView.ViewHolder(view), View.OnClickListener{
        val newsText: TextView = view.findViewById(R.id.news_title)
        val newsImage: ImageView = view.findViewById(R.id.news_image)
        val newsCard: CardView = view.findViewById(R.id.news_item_card)
        val newsConstraintLayout: ConstraintLayout = view.findViewById(R.id.news_item_constraint_layout)

        init{
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return NewsHolder(view)
    }


    override fun getItemCount(): Int {
        return news.size
    }

    fun getPost(position: Int): Post = news[position]

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.newsText.text = news[position].title
        Glide
            .with(context)
            .load(news[position].icon)
            .into(holder.newsImage)
    }
}