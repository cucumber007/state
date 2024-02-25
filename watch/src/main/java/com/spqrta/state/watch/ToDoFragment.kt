package com.spqrta.state.watch

import android.annotation.SuppressLint
import android.app.Fragment
import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.spqrta.state.common.app.action.AppReadyAction
import com.spqrta.state.common.app.action.ToDoListAction
import com.spqrta.state.common.app.features.daily.DailyState
import com.spqrta.state.common.app.features.daily.personas.productive.TodoItem
import com.spqrta.state.common.app.state.optics.AppStateOptics
import kotlinx.coroutines.launch

private const val ARG_TODO_ITEM_POSITION = "todo_item_position"

class ToDoFragment : Fragment(R.layout.fragment_to_do) {
    private var todoItemPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            todoItemPosition = it.getInt(ARG_TODO_ITEM_POSITION, -1)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textView = view.findViewById<TextView>(R.id.textView)
        val tvPageNumber = view.findViewById<TextView>(R.id.tvPageNumber)
        val imageView = view.findViewById<View>(R.id.imageView)
        val todoItem = getState()
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
            if (!getState().checked) {
                scrollViewPager()
            }
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
        tvPageNumber.setOnLongClickListener {
            mainActivity().toggleShowChecked()
            true
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mainActivity().localToDoItemsState.collect { value ->
                    if (getState().checked) {
                        imageView.visibility = View.VISIBLE
                    } else {
                        imageView.visibility = View.GONE
                    }
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getState() = mainActivity()
        .localToDoItemsState.value
        .let {
            if (it.size <= todoItemPosition!!) {
                TodoItem("NO DATA")
            } else {
                it[todoItemPosition!!]
            }
        }

    private fun scrollViewPager() {
        mainActivity().scrollViewPager()
    }

    private fun mainActivity() = requireActivity() as MainActivity

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
