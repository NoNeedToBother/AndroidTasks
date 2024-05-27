package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentGraphBinding

class GraphFragment: Fragment(R.layout.fragment_graph) {

    private val binding: FragmentGraphBinding by viewBinding(FragmentGraphBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        fun newInstance(): GraphFragment = GraphFragment()

        const val GRAPH_FRAGMENT_TAG = "GRAPH_FRAGMENT"
    }
}