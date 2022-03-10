package com.example.mypictureoftheday.view

import android.app.Application
import androidx.room.Room
import com.example.mypictureoftheday.model.ToDoDAO
import com.example.mypictureoftheday.model.ToDoDataBase
import java.lang.Exception

class App: Application() {

    companion object{
        private var appInstance: App? = null
        private var db: ToDoDataBase? = null
        private const val DB_NAME = "ToDo.db"

        fun getToDoDao(): ToDoDAO {
            if (db == null) {
                synchronized(ToDoDataBase::class.java) {
                    if (db == null) {
                        appInstance?.let { app ->
                            db = Room.databaseBuilder(
                                app.applicationContext,
                                ToDoDataBase::class.java,
                                DB_NAME
                            ).build()
                        } ?: throw Exception("")
                    }
                }
            }
            return db!!.toDoDAO()
        }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

}