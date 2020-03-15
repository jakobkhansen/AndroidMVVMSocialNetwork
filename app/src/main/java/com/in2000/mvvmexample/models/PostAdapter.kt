package com.in2000.mvvmexample.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.in2000.mvvmexample.R
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.post.view.*
import java.text.DateFormat

class PostAdapter(val dataSet : List<Post>, val postClickListener: PostClickListener) : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    class PostHolder(var cardView: CardView, var postClickListener: PostClickListener) : RecyclerView.ViewHolder(cardView), View.OnClickListener {

        lateinit var userId : String

        init {
            cardView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            postClickListener.onPostClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false) as CardView
        return PostHolder(view, postClickListener)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.userId = dataSet[position].userId ?: ""
        holder.cardView.apply {
            post_user_name.text = dataSet[position].userFullname
            post_content.text = dataSet[position].content

            val date = dataSet[position].date.toDate()
            val formatter = DateFormat.getDateTimeInstance()
            val dateString = formatter.format(date)
            holder.cardView.post_date.text = "Posted at ${dateString}"

            val imageLink = dataSet[position].userImage
            Glide.with(holder.cardView).load(imageLink).into(holder.cardView.post_user_image)


        }
    }

    interface PostClickListener {
        fun onPostClick(position : Int)
    }
}