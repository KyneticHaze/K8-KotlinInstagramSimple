package com.furkanharmanci.kotlininstagramsimple.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.furkanharmanci.kotlininstagramsimple.databinding.ReyclerItemBinding
import com.furkanharmanci.kotlininstagramsimple.model.Post
import com.squareup.picasso.Picasso
import java.util.ArrayList

class PostAdapter(private var postArrayList : ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(var binding: ReyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ReyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postArrayList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.binding.recyclerEmailText.text = postArrayList[position].email
        holder.binding.recyclerCommentText.text = postArrayList[position].comment
        Picasso.get().load(postArrayList[position].downloadUrl).into(holder.binding.recylerImage)
    }
}