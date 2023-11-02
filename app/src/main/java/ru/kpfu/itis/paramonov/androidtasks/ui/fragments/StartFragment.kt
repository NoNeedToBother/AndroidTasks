package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.MainActivity
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentStartBinding

class StartFragment : Fragment(){
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

    private fun checkFactNumb(s : Editable) : Boolean {
        return s.isNotEmpty() && s.toString().toInt() in 0..45
    }

    private fun init() {
        with(binding) {
            etFactNumb.addTextChangedListener {
                it?.let {
                    if (!checkFactNumb(it)) etFactNumb.error = getString(R.string.error_fact_numb)
                }
            }
            btnProceedFacts.setOnClickListener {
                val factCount = etFactNumb.text.toString()
                if (checkFactNumb(etFactNumb.text)) {
                    (requireActivity() as MainActivity).goToScreen(
                        FactGalleryFragment.newInstance(factCount.toInt()),
                        FactGalleryFragment.FACT_GALLERY_FRAGMENT_TAG,
                        true
                    )
                }
            }
        }
    }

    companion object {
        const val START_FRAGMENT_TAG = "START_FRAGMENT"
    }
}