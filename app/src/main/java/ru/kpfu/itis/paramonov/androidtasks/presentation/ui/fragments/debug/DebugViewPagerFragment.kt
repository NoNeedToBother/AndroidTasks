package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.debug

import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentDebugBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.DebugViewPagerAdapter

class DebugViewPagerFragment: BaseFragment() {
    private val binding: FragmentDebugBinding by viewBinding(FragmentDebugBinding::bind)

    override fun layout(): Int = R.layout.fragment_debug

    override fun init() {
        initViewPager()
    }

    override fun observeData() {}


    private fun initViewPager() {
        with(binding) {
            val adapter = DebugViewPagerAdapter(childFragmentManager, lifecycle)
            val fragmentIdList = listOf(
                R.layout.fragment_debug_info,
                R.layout.fragment_debug_response_log
            )
            adapter.setFragmentIdList(fragmentIdList)

            vpDebug.adapter = adapter
        }
    }

    companion object {
        const val DEBUG_VIEW_PAGER_FRAGMENT_TAG = "DEBUG_VIEW_PAGER_FRAGMENT"
    }
}