package com.example.mypictureoftheday.view.todolist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mypictureoftheday.model.ToDoData

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        abstract fun bind(data: Pair<String, Boolean>)
    }
