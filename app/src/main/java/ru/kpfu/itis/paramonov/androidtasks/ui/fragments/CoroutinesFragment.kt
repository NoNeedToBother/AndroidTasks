package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.MainActivity
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentCoroutinesBinding
import ru.kpfu.itis.paramonov.androidtasks.model.CoroutineConfig
import ru.kpfu.itis.paramonov.androidtasks.util.CoroutineHandler
import ru.kpfu.itis.paramonov.androidtasks.util.NotificationHandler

class CoroutinesFragment : Fragment() {
    private var _binding: FragmentCoroutinesBinding? = null
    private val binding: FragmentCoroutinesBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoroutinesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setCurrentSettings()
        setOnSettingsChangedListeners()
        setOnClickListeners()
    }

    private fun setOnSettingsChangedListeners() {
        with(binding) {
            chkBoxAsync.setOnClickListener {
                CoroutineConfig.isAsync = chkBoxAsync.isChecked
            }
            chkBoxStopOnBackground.setOnClickListener {
                CoroutineConfig.stopOnBackground = chkBoxStopOnBackground.isChecked
            }
            seekBarCoroutines.setOnSeekBarChangeListener(getOnSeekBarChangeListener())
        }
    }

    private fun getOnSeekBarChangeListener() = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekbar: SeekBar?, progress: Int, p2: Boolean) {
            with(binding) {
                tvCoroutinesAmount.text = progress.toString()
                CoroutineConfig.coroutineAmount = progress
            }
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {}

        override fun onStopTrackingTouch(p0: SeekBar?) {}

    }

    private fun setCurrentSettings() {
        with(binding) {
            seekBarCoroutines.progress = CoroutineConfig.coroutineAmount
            tvCoroutinesAmount.text = CoroutineConfig.coroutineAmount.toString()
            chkBoxAsync.isChecked = CoroutineConfig.isAsync
            chkBoxStopOnBackground.isChecked = CoroutineConfig.stopOnBackground
        }
    }

    private fun setOnClickListeners() {
        binding.btnExecuteCoroutines.setOnClickListener {
            if (binding.seekBarCoroutines.progress == 0) {
                Toast.makeText(context, R.string.zero_coroutines, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val coroutineHandler = CoroutineHandler(requireActivity() as MainActivity)
            coroutineHandler.setOnExecutionEndedListener(getOnExecutionEndedListener())
            (requireActivity() as MainActivity).executeCoroutines(coroutineHandler)
        }
    }

    private fun getOnExecutionEndedListener() = object : CoroutineHandler.OnExecutionEndedListener {
        override fun onExecutionEnded(executionStatus: Int) {
            when(executionStatus) {
                CoroutineHandler.EXECUTION_STATUS_SUCCESS ->
                    NotificationHandler(requireContext()).createNotification(
                        getString(R.string.done),
                        getString(R.string.job_done),
                        hasOptions = false,
                        hasLongText = false
                    )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}