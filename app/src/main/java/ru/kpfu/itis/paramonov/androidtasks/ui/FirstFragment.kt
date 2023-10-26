package ru.kpfu.itis.paramonov.androidtasks.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.MainActivity
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding: FragmentFirstBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        checkEditTexts()
        checkQuestionNumber()
    }

    @SuppressLint("ResourceAsColor")
    private fun tryEnableButton() {
        with(binding) {
            if (isPhoneNumberValid(etPhoneNumber.text.toString()) && checkQuestionNumber()) {
                btnGenerate.apply {
                    isEnabled = true
                    setBackgroundColor(R.color.light_green)
                    setOnClickListener {
                        (requireActivity() as MainActivity).goToScreen(
                            TestQuestionsFragment.newInstance(etQuestionNumb.text.toString().toInt()),
                            TestQuestionsFragment.TEST_QUESTIONS_FRAGMENT_TAG,
                            false
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun disableButton() {
        with(binding.btnGenerate) {
            isEnabled = false
            setBackgroundColor(R.color.button_default)
        }
    }

    private fun EditText.addSymbols(s : String) {
        setText(text.toString() + s)
        setSelection(text.length)
    }

    private fun getPhoneNumbTextWatcher(et : EditText) : TextWatcher {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.apply {
                    if (count != 0 && checkFormatNecessity(s)) {
                        when (length) {
                            7 -> et.addSymbols(")-")
                            12, 15 -> et.addSymbols("-")
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (!isPhoneNumberValid(it)) {
                        et.error = getString(R.string.incorrect_phone_numb)
                        disableButton()
                    } else {
                        tryEnableButton()
                    }
                }
            }
        }
        return watcher
    }

    private fun checkFormatNecessity(phoneNumber : CharSequence) : Boolean{
        val regEx =  """\+7 \(9(\d{2}$|\d{2}\)-\d{3}$|\d{2}\)-\d{3}-\d{2}$)$""".toRegex()
        return regEx.find(phoneNumber) != null
    }

    private fun isPhoneNumberValid(phoneNumber : CharSequence) : Boolean {
        val regEx =  """\+7 \(9\d{2}\)-\d{3}(-\d{2}){2}$""".toRegex()
        return regEx.find(phoneNumber) != null
    }

    private fun checkEditTexts() {
        with(binding) {
            etPhoneNumber.apply {
                setOnClickListener {
                    if (text.isEmpty()) addSymbols("+7 (9")
                }
                addTextChangedListener(getPhoneNumbTextWatcher(this@apply))
            }

            etQuestionNumb.apply {
                addTextChangedListener(getQuestionNumbTextWatcher(this@apply))
            }
        }
    }

    private fun getQuestionNumbTextWatcher(et : EditText) = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (!checkQuestionNumber()) {
                et.error = getString(R.string.incorrect_question_numb)
                disableButton()
            } else {
                tryEnableButton()
            }
        }


    }

    private fun checkQuestionNumber() : Boolean {
        with(binding) {
            etQuestionNumb.apply {
                return if (!text.isNullOrEmpty()) {
                    val questionNumber = text.toString().toInt()
                    questionNumber in 1..10
                } else {
                    false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val FIRST_FRAGMENT_TAG = "FIRST_FRAGMENT"
    }


}