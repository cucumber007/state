package com.spqrta.state.watch

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.spqrta.state.common.app.action.AppReadyAction
import com.spqrta.state.common.app.action.ToDoListAction
import com.spqrta.state.common.app.features.daily.DailyState
import com.spqrta.state.common.app.state.optics.AppStateOptics
import kotlinx.coroutines.launch

private const val ARG_TODO_ITEM_POSITION = "todo_item_position"

class ToDoFragment : Fragment() {
    private var todoItemPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            todoItemPosition = it.getInt(ARG_TODO_ITEM_POSITION, -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_to_do, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textView = view.findViewById<TextView>(R.id.textView)
        val tvPageNumber = view.findViewById<TextView>(R.id.tvPageNumber)
        val imageView = view.findViewById<View>(R.id.imageView)
        val todoItem = AppStateOptics
            .optTodoList
            .get(WatchApplication.app.state.value)!!
            .items[todoItemPosition!!]
        textView.text = todoItem.title
        val currentPage = (todoItemPosition!! + 1)
        val totalPages = AppStateOptics
            .optTodoList
            .get(WatchApplication.app.state.value)!!
            .items.size
        tvPageNumber.text = "$currentPage / $totalPages"
        if (todoItem.checked) {
            imageView.visibility = View.VISIBLE
        } else {
            imageView.visibility = View.GONE
        }
        view.setOnClickListener {
            WatchApplication.app.handleAction(
                ToDoListAction.OnPress(todoItem.title)
            )
        }
        view.setOnLongClickListener {
            WatchApplication.app.handleAction(
                AppReadyAction.ResetDayAction(DailyState.INITIAL_WATCH)
            )
            Toast.makeText(
                requireContext(),
                "Day reset",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                WatchApplication.app.state.collect { value ->
                    val todoItem = AppStateOptics
                        .optTodoList
                        .get(WatchApplication.app.state.value)!!
                        .items[todoItemPosition!!]
                    if (todoItem.checked) {
                        imageView.visibility = View.VISIBLE
                    } else {
                        imageView.visibility = View.GONE
                    }
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(todoItemPosition: Int) =
            ToDoFragment().apply {
                arguments = Bundle().apply {
                    putInt(
                        ARG_TODO_ITEM_POSITION,
                        todoItemPosition
                    )
                }
            }
    }
}
