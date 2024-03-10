package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.DebugInfoFragment
import ru.kpfu.itis.paramonov.androidtasks.utils.ResManager
import java.lang.RuntimeException

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val resManager: ResManager):
    FragmentStateAdapter(fragmentManager, lifecycle){

    private val fragmentIdList = mutableListOf<Int>()

    override fun getItemCount(): Int {
        return fragmentIdList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(fragmentIdList[position]) {
            R.layout.fragment_debug_info -> DebugInfoFragment(resManager)
            else -> throw RuntimeException()
        }
    }

    fun setFragmentIdList(fragments: List<Int>) {
        fragmentIdList.addAll(fragments)
    }

}