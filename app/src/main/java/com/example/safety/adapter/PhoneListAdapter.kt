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
import com.example.safety.model.PhoneNumber

class PhoneListAdapter:ListAdapter<PhoneNumber, PhoneListAdapter.PhoneViewHolder>(WordsComparator()) {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneViewHolder {
        return PhoneViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PhoneViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.phoneNumber)
        holder.btnDelete.setOnClickListener {
            if (onClickListener!=null){
                onClickListener!!.onClick(current.id)
            }
        }
    }

    class PhoneViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        private val phoneView: TextView = itemView.findViewById(R.id.tvContact)
        val btnDelete:ImageButton = itemView.findViewById(R.id.btnDeleteContact)

        fun bind(text: String?) {
            phoneView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): PhoneViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_contact_list, parent, false)
                return PhoneViewHolder(view)
            }
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<PhoneNumber>() {
        override fun areItemsTheSame(oldItem: PhoneNumber, newItem: PhoneNumber): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PhoneNumber, newItem: PhoneNumber): Boolean {
            return oldItem.phoneNumber == newItem.phoneNumber
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