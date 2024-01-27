package com.spqrta.state.watch

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spqrta.state.common.app.state.optics.AppStateOptics

class MyPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = getTodoList()?.items?.size ?: 0

    override fun createFragment(position: Int): Fragment {
        return ToDoFragment.newInstance(position)
    }

    private fun getTodoList() = WatchApplication.app.state.value.let {
        AppStateOptics.optTodoList.get(it)
    }
}
