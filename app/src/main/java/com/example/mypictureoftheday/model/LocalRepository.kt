package com.example.mypictureoftheday.model

interface LocalRepository {

    fun getAllToDoItems(): List<ToDoData>

    fun saveEntity(toDoItem: ToDoData)

    fun deleteEntity(itemText: String)
}