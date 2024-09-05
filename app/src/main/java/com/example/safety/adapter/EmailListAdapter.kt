package com.example.safety.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.safety.R
import com.example.safety.model.Email

class EmailListAdapter: ListAdapter<Email, EmailListAdapter.EmailViewHolder>(WordsComparator()) {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        return EmailViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.email)
        holder.btnDelete.setOnClickListener {
            if (onClickListener!=null){
                onClickListener!!.onClick(current.id)
            }
        }
    }

    class EmailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val emailView: TextView = itemView.findViewById(R.id.tvContact)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteContact)

        fun bind(text: String?) {
            emailView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): EmailViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_contact_list, parent, false)
                return EmailViewHolder(view)
            }
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<Email>() {
        override fun areItemsTheSame(oldItem: Email, newItem: Email): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Email, newItem: Email): Boolean {
            return oldItem.email == newItem.email
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener)
    {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(id: Int)
    }
}