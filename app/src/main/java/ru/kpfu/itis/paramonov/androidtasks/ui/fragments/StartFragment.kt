package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding: FragmentStartBinding get() = _binding!!

    companion object {
        const val START_FRAGMENT_TAG = "START_FRAGMENT"
    }

}