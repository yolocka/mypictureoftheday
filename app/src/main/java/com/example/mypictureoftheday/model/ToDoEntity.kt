package com.example.mypictureoftheday.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ToDoEntity")
data class ToDoEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val toDoItem: String,
    val isToDoItemExpanded: Boolean
)