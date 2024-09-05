package com.example.safety.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safety.R
import com.example.safety.model.Comment

class CommentAdapter(private val commentList:List<Comment>):RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val userName:TextView = view.findViewById(R.id.tv_name)
        val date:TextView = view.findViewById(R.id.tv_date)
        val time:TextView = view.findViewById(R.id.tv_time)
        val comment:TextView = view.findViewById(R.id.tv_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = commentList[position]
        holder.userName.text = comment.name
        holder.date.text = comment.date
        holder.time.text = comment.time
        holder.comment.text = comment.comment
    }
}