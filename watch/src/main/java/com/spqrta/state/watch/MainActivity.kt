package com.spqrta.state.watch

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import androidx.wear.ambient.AmbientModeSupport
import com.spqrta.state.common.app.features.daily.personas.productive.ToDoList
import com.spqrta.state.common.app.features.daily.personas.productive.TodoItem
import com.spqrta.state.common.app.state.optics.AppStateOptics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity : AppCompatActivity(), AmbientModeSupport.AmbientCallbackProvider {

    private var viewPager: ViewPager2? = null
    private var scrollSavedState: Float = 0f
    private lateinit var ambientController: AmbientModeSupport.AmbientController
    private var showChecked = true
    val localToDoItemsState = MutableStateFlow(listOf<TodoItem>())

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewPager)
        val pagerAdapter = MyPagerAdapter(this)
        viewPager!!.adapter = pagerAdapter
        var lastTodoListState = AppStateOptics.optTodoList.get(WatchApplication.app.state.value)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                WatchApplication.app.state.collect { value ->
                    val todoListState = AppStateOptics.optTodoList.get(value)
                    if (todoListState != lastTodoListState) {
                        lastTodoListState = todoListState
                        updateLocalState()
                    }
                }
            }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                localToDoItemsState.collect {
                    pagerAdapter.notifyDataSetChanged()
                }
            }
        }
        ambientController = AmbientModeSupport.attach(this)
    }

    @SuppressLint("InlinedApi")
    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_SCROLL) {
            val scrollAmount = event.getAxisValue(MotionEvent.AXIS_SCROLL)
            Log.d("state-scroll", "scrollAmount: $scrollAmount")
            if ((scrollSavedState > 0 && scrollAmount < 0) || (scrollSavedState < 0 && scrollAmount > 0)) {
                scrollSavedState = 0f
                Log.d("state-scroll", "scrollSavedState zeroed")
            }
            scrollSavedState += scrollAmount
            Log.d("state-scroll", "scrollSavedState: $scrollSavedState")
            if (abs(scrollSavedState) > 1.5f) {
                if (scrollSavedState > 0) {
                    val nextItem = viewPager!!.currentItem + 1
                    viewPager!!.setCurrentItem(nextItem, true)
                } else {
                    val previousItem = viewPager!!.currentItem - 1
                    viewPager!!.setCurrentItem(previousItem, true)
                }
                scrollSavedState = 0f
            }
            return true
        }
        return super.onGenericMotionEvent(event)
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
        return object : AmbientModeSupport.AmbientCallback() {
            override fun onEnterAmbient(ambientDetails: Bundle?) {
                // Handle entering ambient mode
            }

            override fun onExitAmbient() {
                // Handle exiting ambient mode
            }

            override fun onUpdateAmbient() {
                // Update display in ambient mode
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun scrollViewPager() {
        viewPager?.let { viewPager ->
            if (viewPager.currentItem < viewPager.adapter!!.itemCount - 1) {
                viewPager.currentItem = viewPager.currentItem + 1
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun toggleShowChecked() {
        showChecked = !showChecked
        updateLocalState()
    }

    private fun updateLocalState() {
        lifecycleScope.launch {
            localToDoItemsState.emit(
                if (showChecked) {
                    getToDoList()!!.items
                } else {
                    ToDoList.optUnchecked.get(getToDoList()!!)!!
                }
            )
        }
    }

    private fun getToDoList(): ToDoList? {
        return WatchApplication.app.state.value
            .let {
                AppStateOptics.optTodoList.get(it)
            }
    }

}
