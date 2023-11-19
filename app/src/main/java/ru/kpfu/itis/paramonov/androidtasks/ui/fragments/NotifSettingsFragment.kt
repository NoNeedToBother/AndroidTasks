package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentNotifSettingsBinding

class NotifSettingsFragment : Fragment() {
    private var _binding: FragmentNotifSettingsBinding? = null
    private val binding: FragmentNotifSettingsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotifSettingsBinding.inflate(inflater)
        return binding.root
    }
}