package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentDebugResponseLogBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.DebugResponseLogViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.appComponent
import javax.inject.Inject

class DebugResponseLogFragment: Fragment(R.layout.fragment_debug_response_log) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: DebugResponseLogViewModel by viewModels { factory }

    private val binding: FragmentDebugResponseLogBinding by viewBinding(FragmentDebugResponseLogBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}