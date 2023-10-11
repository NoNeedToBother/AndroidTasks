package ru.kpfu.itis.paramonov.androidtasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentThirdBinding

class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private val binding: FragmentThirdBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //_binding = FragmentThirdBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentThirdBinding.bind(view)
        setScreenText()
    }

    private fun setScreenText() {
        with(binding) {
            val text = arguments?.getString(FirstFragment.TEXT_ID)
            if (text.isNullOrEmpty()) {
                tvScreenText.text = getString(R.string.third_screen_standard)
            } else {
                tvScreenText.text = text
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val THIRD_FRAGMENT_TAG = "THIRD_FRAGMENT"

        fun newInstance(text: String) = ThirdFragment().apply {
            arguments = bundleOf(FirstFragment.TEXT_ID to text)
        }
    }
}