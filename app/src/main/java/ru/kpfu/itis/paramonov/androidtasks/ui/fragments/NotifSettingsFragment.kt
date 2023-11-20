package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentNotifSettingsBinding
import ru.kpfu.itis.paramonov.androidtasks.model.Importance
import ru.kpfu.itis.paramonov.androidtasks.model.NotificationConfig
import ru.kpfu.itis.paramonov.androidtasks.model.Visibility

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        Toast.makeText(context, NotificationConfig.visibility.toString(), Toast.LENGTH_LONG).show()
    }

    private fun init() {
        populateSpinners()
        setCurrentSettings()
        setSettingChangedListeners()
    }

    private fun getOnItemSelectedListenerImportance() = object: OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
            val importance = parent?.getItemAtPosition(pos) as String
            saveImportance(Importance.valueOf(importance))
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            //parent?.setSelection(getPositionImportance())
        }
    }

    private fun getOnItemSelectedListenerVisibility() = object: OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
            val visibility = parent?.getItemAtPosition(pos) as String
            saveVisibility(Visibility.valueOf(visibility))
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            //parent?.setSelection(getPositionImportance())
        }
    }

    private fun saveImportance(importance: Importance) {
        NotificationConfig.importance = importance
    }

    private fun saveVisibility(visibility: Visibility) {
        NotificationConfig.visibility = visibility
    }

    private fun populateSpinners() {
        val importances = ArrayList<String>()
        for (importance in Importance.values()) {
            importances.add(importance.toString())
        }

        val visibilities = ArrayList<String>()
        for (visibility in Visibility.values()) {
            visibilities.add(visibility.toString())
        }

        with(binding) {
            spinnerImportance.adapter = getAdapter(importances.toTypedArray())
            spinnerVisibility.adapter = getAdapter(visibilities.toTypedArray())
        }
    }

    private fun setSettingChangedListeners() {
        with(binding) {
            spinnerImportance.onItemSelectedListener = getOnItemSelectedListenerImportance()
            spinnerVisibility.onItemSelectedListener = getOnItemSelectedListenerVisibility()
            chkBoxOptions.setOnClickListener{
                NotificationConfig.hasOptions = chkBoxOptions.isChecked
            }
            chkBoxLongText.setOnClickListener{
                NotificationConfig.hasLongText = chkBoxLongText.isChecked
            }
        }
    }

    private fun getAdapter(array : Array<String>): ArrayAdapter<String>{
        return ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            array
        ).apply {
            setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item)
        }
    }

    private fun setCurrentSettings() {
        with(binding) {
            spinnerImportance.setSelection(getPositionImportance())
            spinnerVisibility.setSelection(getPositionVisibility())
            chkBoxLongText.isChecked = NotificationConfig.hasLongText
            chkBoxOptions.isChecked = NotificationConfig.hasOptions
        }
    }

    private fun getPositionImportance(): Int {
        return Importance.values().indexOf(NotificationConfig.importance)
    }

    private fun getPositionVisibility(): Int {
        return Visibility.values().indexOf(NotificationConfig.visibility)
    }


}