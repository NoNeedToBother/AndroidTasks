package ru.kpfu.itis.paramonov.androidtasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding: FragmentFirstBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //_binding = FragmentFirstBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFirstBinding.bind(view)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnSubmit.setOnClickListener {
                (requireActivity() as MainActivity).goToScreen(
                    SecondFragment.newInstance(etText.text.toString()),
                    SecondFragment.SECOND_FRAGMENT_TAG,
                    true
                )
                (requireActivity() as MainActivity).goToScreen(
                    ThirdFragment.newInstance(etText.text.toString()),
                    ThirdFragment.THIRD_FRAGMENT_TAG,
                    true
                )
            }
        }
        /*binding?.btnSubmit?.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager

            if (binding?.etText?.text?.toString().isNullOrEmpty()) {
                /*fragmentManager.beginTransaction()
                    .add(
                        R.id.main_activity_container,
                        SecondFragment(),
                        SecondFragment.SECOND_FRAGMENT_TAG
                    )
                    .addToBackStack(null)
                    .commit()*/

                requireActivity().supportFragmentManager.beginTransaction()
                    .add(
                        R.id.main_activity_container,
                        ThirdFragment(),
                        ThirdFragment.THIRD_FRAGMENT_TAG
                    )
                    .addToBackStack(null)
                    .commit()
            } else {
                fragmentManager.beginTransaction()
                    .add(
                        R.id.main_activity_container,
                        SecondFragment.newInstance(binding?.etText?.text.toString()),
                        SecondFragment.SECOND_FRAGMENT_TAG
                    )
                    .addToBackStack(null)
                    .commit()

                requireActivity().supportFragmentManager.beginTransaction()
                    .add(
                        R.id.main_activity_container,
                        ThirdFragment.newInstance(binding?.etText?.text.toString()),
                        ThirdFragment.THIRD_FRAGMENT_TAG
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val FIRST_FRAGMENT_TAG = "FIRST_FRAGMENT"
        const val TEXT_ID = "text"
    }
}