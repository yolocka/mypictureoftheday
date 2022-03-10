package com.example.mypictureoftheday.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mypictureoftheday.model.LocalRepository
import com.example.mypictureoftheday.model.LocalRepositoryImpl
import com.example.mypictureoftheday.model.ToDoData
import com.example.mypictureoftheday.view.App

class ToDoViewModel: ViewModel() {

    private val localRepo: LocalRepository = LocalRepositoryImpl(App.getToDoDao())

    fun getToDoList(): List<ToDoData> {
        return localRepo.getAllToDoItems()
    }

    fun saveToDoItem(toDoItem: ToDoData) {
        localRepo.saveEntity(toDoItem)
    }

    fun deleteToDoItem(toDoItem: ToDoData) {
        localRepo.deleteEntity(toDoItem.toDoItem)
    }
}