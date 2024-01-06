package com.spqrta.state.watch_old

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

private const val ARG_TODO_ITEM = "todo_item"

class ToDoFragment : Fragment() {
    private var todoItem: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            todoItem = it.getString(ARG_TODO_ITEM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_to_do, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = todoItem
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(todoItem: String) =
            ToDoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TODO_ITEM, todoItem)
                }
            }
    }
}
