package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.app.Notification
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentStartBinding
import ru.kpfu.itis.paramonov.androidtasks.util.NotificationHandler

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnCreateNotif.setOnClickListener {
                val notificationHandler = NotificationHandler(requireContext())
                notificationHandler.createNotification(
                    etTitle.text.toString(),
                    etContent.text.toString()
                )
            }
        }
    }

    private fun createNotification() {

    }

}