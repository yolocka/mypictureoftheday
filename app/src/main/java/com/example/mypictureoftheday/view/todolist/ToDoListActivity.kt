package com.example.mypictureoftheday.view.todolist

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypictureoftheday.R
import com.example.mypictureoftheday.model.ToDoData
import com.example.mypictureoftheday.view.MainActivity
import com.example.mypictureoftheday.viewmodel.ToDoViewModel
import kotlinx.android.synthetic.main.activity_to_do_list.*

class ToDoListActivity : AppCompatActivity() {

    private val sharedPref: SharedPreferences by lazy {
        getSharedPreferences(MainActivity.SHAR_PREF_NAME, Context.MODE_PRIVATE)
    }
    private val viewModel: ToDoViewModel by lazy {
        ViewModelProvider(this).get(ToDoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        when (sharedPref.getString(MainActivity.PREF_THEME, "")) {
            MainActivity.PLUM_THEME -> setTheme(R.style.PlumTheme)
            MainActivity.MANDARIN_THEME -> setTheme(R.style.MandarinTheme)
            MainActivity.BLUEBERRY_THEME -> setTheme(R.style.BlueberryTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)

        val data = viewModel.getToDoList()
        var todoItemsList: MutableList<Pair<String, Boolean>> = arrayListOf(Pair("Header", false))

        data.forEach {
            todoItemsList.add(Pair(it.toDoItem, it.isToDoItemExpanded))
        }


        val adapter = ToDoListAdapter(
            object : ToDoListAdapter.OnListItemClickListener {
                override fun onItemClick(todoData: ToDoData) {
                }
            },
            todoItemsList
        )

        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        recyclerView.adapter = adapter
    }
}