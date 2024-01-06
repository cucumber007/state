package com.spqrta.state.watch_old

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2 // replace with your number of pages

    override fun createFragment(position: Int): Fragment {
        return ToDoFragment.newInstance(position.toString())
    }
}
