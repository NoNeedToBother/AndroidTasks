package ru.kpfu.itis.paramonov.androidtasks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.adapter.QuestionFragmentAdapter
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentTestQuestionsBinding
import ru.kpfu.itis.paramonov.androidtasks.util.QuestionGenerator

class TestQuestionsFragment : Fragment(){
    private var _binding: FragmentTestQuestionsBinding? = null
    private val binding: FragmentTestQuestionsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestQuestionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val questionsNumber = arguments?.getInt("question_numb")
        questionsNumber?.let {
            val generator = QuestionGenerator(questionsNumber)
            val questions = generator.generateQuestions()

            with(binding) {
                vpTest.apply {
                    adapter = QuestionFragmentAdapter(questions, parentFragmentManager, lifecycle)
                    registerOnPageChangeCallback(getCallback(questionsNumber, this@apply))
                    setCurrentItem(1, false)
                }
            }
        }
    }

    private fun getCallback(questionNumb: Int, vp : ViewPager2) : OnPageChangeCallback {
        with(binding) {
            return object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val realPos = when(position) {
                        0 -> questionNumb
                        questionNumb + 1 -> 1
                        else -> position
                    }
                    tvQuestionNumb.text = getString(R.string.question_pos, realPos, questionNumb)

                    when(position) {
                        0 -> vp.setCurrentItem(questionNumb, false)
                        questionNumb + 1 -> vp.setCurrentItem(1, false)
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TEST_QUESTIONS_FRAGMENT_TAG = "TEST_QUESTIONS_FRAGMENT"
        fun newInstance(questionNumb : Int) = TestQuestionsFragment().apply {
            arguments = bundleOf("question_numb" to questionNumb)
        }
    }

}