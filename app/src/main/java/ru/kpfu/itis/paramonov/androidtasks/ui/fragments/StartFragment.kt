package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding: FragmentStartBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

}