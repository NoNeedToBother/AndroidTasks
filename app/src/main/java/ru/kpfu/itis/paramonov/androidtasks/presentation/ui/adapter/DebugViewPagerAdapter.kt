package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.debug.DebugInfoFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.debug.DebugResponseLogFragment
import java.lang.RuntimeException

class DebugViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle){

    private val fragmentIdList = mutableListOf<Int>()

    override fun getItemCount(): Int {
        return fragmentIdList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(fragmentIdList[position]) {
            R.layout.fragment_debug_info -> DebugInfoFragment()
            R.layout.fragment_debug_response_log -> DebugResponseLogFragment()
            else -> throw RuntimeException()
        }
    }

    fun setFragmentIdList(fragments: List<Int>) {
        fragmentIdList.addAll(fragments)
    }

}