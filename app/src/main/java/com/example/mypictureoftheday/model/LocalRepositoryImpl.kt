package com.example.mypictureoftheday.model

import kotlinx.coroutines.runBlocking

class LocalRepositoryImpl(private val dao: ToDoDAO): LocalRepository {

    override fun getAllToDoItems(): List<ToDoData> = runBlocking {
        dao.all()
            .map { entity ->
                ToDoData(
                    toDoItem = entity.toDoItem,
                ) }
    }

    override fun saveEntity(toDoItem: ToDoData) = runBlocking {
        dao.insert(
            ToDoEntity(
                id = 0,
                toDoItem = toDoItem.toDoItem,
                isToDoItemExpanded = toDoItem.isToDoItemExpanded
            )
        )
    }

    override fun deleteEntity(itemText: String) = runBlocking {
        dao.delete(itemText)
    }
}