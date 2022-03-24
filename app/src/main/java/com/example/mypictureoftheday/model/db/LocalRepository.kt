package com.example.mypictureoftheday.model.db

interface LocalRepository {

    fun getAllToDoItems(): List<ToDoData>

    fun saveEntity(toDoItem: ToDoData)

    fun deleteEntity(itemText: String)
}