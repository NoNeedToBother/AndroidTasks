package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentBsdDialogBinding
import ru.kpfu.itis.paramonov.androidtasks.util.CityFactsRepository

class AddFactsBsdFragment(private val onFactsAdded: () -> Unit) : BottomSheetDialogFragment() {
    private var _binding: FragmentBsdDialogBinding? = null
    private val binding: FragmentBsdDialogBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBsdDialogBinding.inflate(inflater)
        return binding.root
    }

    private fun checkFactNumb(s : Editable) : Boolean {
        return s.isNotEmpty() && s.toString().toInt() in 0..5
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            etFactToAdd.addTextChangedListener {
                it?.let {
                    if (!checkFactNumb(it)) etFactToAdd.error = getString(R.string.error_add_fact_numb)
                }
            }

            btnAddFacts.setOnClickListener {
                val addFactAmount = etFactToAdd.text
                if(checkFactNumb(addFactAmount)) {
                    CityFactsRepository.addFacts(addFactAmount.toString().toInt())
                    onFactsAdded()
                    dismiss()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ADD_FACTS_BSD_FRAGMENT_TAG = "ADD_FACTS_BSD_FRAGMENT"
    }
}