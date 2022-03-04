package com.example.mypictureoftheday.model

import androidx.room.*

@Dao
interface ToDoDAO {
    @Query("SELECT * FROM ToDoEntity ORDER BY id")
    suspend fun all(): List<ToDoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: ToDoEntity)

    @Query("DELETE FROM ToDoEntity WHERE toDoItem=:itemText")
    suspend fun delete(itemText: String)
}