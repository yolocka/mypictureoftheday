package com.example.mypictureoftheday.view.todolist

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypictureoftheday.R
import com.example.mypictureoftheday.model.db.LocalRepository
import com.example.mypictureoftheday.model.db.LocalRepositoryImpl
import com.example.mypictureoftheday.model.db.ToDoData
import com.example.mypictureoftheday.view.App
import kotlinx.android.synthetic.main.todo_item.view.*
import kotlinx.android.synthetic.main.todo_item_edit.view.*

class ToDoListAdapter(
    private var listener: OnListItemClickListener,
    private var todoList: MutableList<Pair<String, Boolean>>
    ) : RecyclerView.Adapter<BaseViewHolder>() {

    private val localRepo: LocalRepository = LocalRepositoryImpl(App.getToDoDao())

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    fun appendItem(item: CharSequence?) {
        todoList.add(Pair(item.toString(), false))
        localRepo.saveEntity(ToDoData(item.toString(), false))
        notifyItemInserted(itemCount)
    }

    fun removeItem(layoutPosition: Int) {
        todoList.removeAt(layoutPosition)
        notifyItemRemoved(layoutPosition)
    }


    inner class TextViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: Pair<String, Boolean>) {
            itemView.apply {
                val spannableItemText = SpannableString(data.first)
                spannableItemText.setSpan(
                    ForegroundColorSpan(Color.RED),
                    0, 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                findViewById<AppCompatTextView>(R.id.todo_text_view).setText(spannableItemText, TextView.BufferType.SPANNABLE)
                removeItemImageView.setOnClickListener {
                    localRepo.deleteEntity(data.first)
                    removeItem(layoutPosition)
                }
                setOnClickListener {
                    listener.onItemClick(ToDoData(data.first, data.second))
                }
                moveItemDown.setOnClickListener { moveDown() }
                moveItemUp.setOnClickListener { moveUp() }

                todo_text_view.setOnClickListener { toggleText() }

                if (data.second) {
                    removeItemImageView.visibility = View.VISIBLE
                    moveItemUp.visibility = View.VISIBLE
                    moveItemDown.visibility = View.VISIBLE
                } else {
                    removeItemImageView.visibility = View.INVISIBLE
                    moveItemUp.visibility = View.INVISIBLE
                    moveItemDown.visibility = View.INVISIBLE
                }
            }
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                todoList.removeAt(currentPosition).apply {
                    todoList.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < todoList.size - 1 }?.also { currentPosition ->
                todoList.removeAt(currentPosition).apply {
                    todoList.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }



        private fun toggleText() {
            todoList[layoutPosition] = todoList[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }
    }

    inner class EditTextViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: Pair<String, Boolean>) {
            itemView.todo_edit_text.setEndIconOnClickListener{
                if (!itemView.input_edit_text.text.isNullOrEmpty()) {
                    appendItem(itemView.input_edit_text.text)
                    itemView.input_edit_text.setText("")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_TEXT_VIEW) {
            TextViewHolder(inflater.inflate(R.layout.todo_item, parent, false) as View)
        } else {
            EditTextViewHolder(inflater.inflate(R.layout.todo_item_edit, parent, false) as View)
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (todoList[position].first == HEADER) TYPE_EDIT_VIEW else TYPE_TEXT_VIEW
    }

    interface OnListItemClickListener {
        fun onItemClick(data: ToDoData)
    }

    companion object {
        private const val TYPE_EDIT_VIEW = 0
        private const val TYPE_TEXT_VIEW = 1
        private const val HEADER = "Header"
    }
}