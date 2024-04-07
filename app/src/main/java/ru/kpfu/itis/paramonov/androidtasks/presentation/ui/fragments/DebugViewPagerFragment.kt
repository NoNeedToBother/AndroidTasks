package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentDebugBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.DebugViewPagerAdapter

class DebugViewPagerFragment: Fragment(R.layout.fragment_debug) {
    private val binding: FragmentDebugBinding by viewBinding(FragmentDebugBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        with(binding) {
            val adapter = DebugViewPagerAdapter(parentFragmentManager, lifecycle)
            val fragmentIdList = listOf(
                R.layout.fragment_debug_info
            )
            adapter.setFragmentIdList(fragmentIdList)

            vpDebug.adapter = adapter
        }
    }

    companion object {
        const val DEBUG_VIEW_PAGER_FRAGMENT_TAG = "DEBUG_VIEW_PAGER_FRAGMENT"
    }
}