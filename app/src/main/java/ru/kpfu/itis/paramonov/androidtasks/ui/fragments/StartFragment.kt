package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.kpfu.itis.paramonov.androidtasks.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnGoGrid.setOnClickListener {
                if (etElemAmount.text.isEmpty()) {
                    Toast.makeText(context, R.string.empty_amount, Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                findNavController().navigate(
                    R.id.action_startFragment_to_staggeredGridFragment,
                    bundleOf("item_amount" to etElemAmount.text.toString().toInt())
                )
            }
        }
    }

}