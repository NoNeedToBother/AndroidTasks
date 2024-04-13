package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments

import android.content.Context
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentDebugResponseLogBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.ResponseLogAdapter
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.DebugResponseLogViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager
import ru.kpfu.itis.paramonov.androidtasks.utils.SpacingItemDecorator
import ru.kpfu.itis.paramonov.androidtasks.utils.appComponent
import javax.inject.Inject

class DebugResponseLogFragment: BaseFragment(R.layout.fragment_debug_response_log) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: DebugResponseLogViewModel by viewModels { factory }

    private val binding: FragmentDebugResponseLogBinding by viewBinding(FragmentDebugResponseLogBinding::bind)

    @Inject
    lateinit var resourceManager: ResourceManager

    private var adapter: ResponseLogAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    override fun init() {
        initRecyclerView()
        viewModel.getResponses()
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectResponseLog()
                }
            }
        }
    }

    private suspend fun collectResponseLog() {
        viewModel.responseLogFlow.collect { result ->
            result?.let {
                when (it) {
                    is DebugResponseLogViewModel.ResponseDataResult.Success ->
                        adapter?.submitList(it.getValue())
                    is DebugResponseLogViewModel.ResponseDataResult.Failure ->
                        showError(it.getException())
                }
            }
        }
    }

    private fun initRecyclerView() {
        val adapter = ResponseLogAdapter(
            onResponseClicked = ::onResponseClicked,
            resourceManager = resourceManager
        )
        this.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())

        with(binding.rvResponses) {
            this.adapter = adapter
            this.layoutManager = layoutManager
            addItemDecoration(SpacingItemDecorator(24, ru.kpfu.itis.paramonov.androidtasks.utils.SpacingItemDecorator.Side.BOTTOM))
        }
    }

    private fun onResponseClicked(pos: Int) {
        requireActivity().supportFragmentManager.commit {
            replace(
                R.id.main_activity_container,
                DebugResponseFragment.newInstance(pos),
                DebugResponseFragment.DEBUG_RESPONSE_FRAGMENT_TAG
            )
            addToBackStack(null)
        }
    }
}