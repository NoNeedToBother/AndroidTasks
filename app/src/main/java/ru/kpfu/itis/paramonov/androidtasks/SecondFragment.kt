package ru.kpfu.itis.paramonov.androidtasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding: FragmentSecondBinding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //_binding = FragmentSecondBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSecondBinding.bind(view)
        //clearBackStack()
        setScreenText()
        setOnClickListeners()
    }

    private fun setScreenText() {
        with(binding) {
            val text = arguments?.getString(FirstFragment.TEXT_ID)
            if (text.isNullOrEmpty()) {
                tvScreenText.text = getString(R.string.second_screen_standard)
            } else {
                tvScreenText.text = text
            }
        }
    }

    private fun clearBackStack() {
        val fm: FragmentManager = requireActivity().supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnFirstScr.setOnClickListener {
                clearBackStack()
                (requireActivity() as MainActivity).goToScreen(
                    FirstFragment(),
                    FirstFragment.FIRST_FRAGMENT_TAG,
                    false
                )
            }

            btnThirdScr.setOnClickListener {
                clearBackStack()
                (requireActivity() as MainActivity).goToScreen(
                    FirstFragment(),
                    FirstFragment.FIRST_FRAGMENT_TAG,
                    true
                )

                (requireActivity() as MainActivity).goToScreen(
                    ThirdFragment(),
                    ThirdFragment.THIRD_FRAGMENT_TAG,
                    true
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val SECOND_FRAGMENT_TAG = "SECOND_FRAGMENT"

        fun newInstance(text: String) = SecondFragment().apply {
            arguments = bundleOf(FirstFragment.TEXT_ID to text)
        }
    }
}