package ru.kpfu.itis.paramonov.androidtasks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.adapter.OptionAdapter
import ru.kpfu.itis.paramonov.androidtasks.model.Question
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment() {
    private var _binding: FragmentQuestionBinding? = null
    private val binding: FragmentQuestionBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun updateChoice(pos: Int) {
        with(binding.rvOptions) {
            adapter?.run {
                this@run as OptionAdapter
                for (i in 0..itemCount - 1) {
                    if (options[i].checked) {
                        options[i].checked = false
                        notifyItemChanged(i)
                    }
                }

                options[pos].checked = true
                notifyItemChanged(pos)

            }
        }
    }


    private fun init() {
        val question = arguments?.getSerializable("question") as Question

        with(binding) {
            tvQuestion.text = question.text
            rvOptions.adapter = OptionAdapter(
                question.options,
                onItemChecked = {pos -> updateChoice(pos)},
                onRootClicked = {pos -> updateChoice(pos)}
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(question: Question, questionPosition : Int) = QuestionFragment().apply {
            arguments = bundleOf("question" to question, "pos" to questionPosition)
        }
    }
}