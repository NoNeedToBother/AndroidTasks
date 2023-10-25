package ru.kpfu.itis.paramonov.androidtasks

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import org.w3c.dom.Text
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding: FragmentFirstBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        checkText()
        checkQuestionNumber()
        enableButton()
    }

    private fun enableButton() {
        with(binding) {
            if (isPhoneNumberValid(etPhoneNumber.text.toString()) && checkQuestionNumber()) {
                btnGenerate.isClickable = true
            }
        }
    }

    private fun EditText.addSymbols(s : String) {
        setText(text.toString() + s)
        setSelection(text.length)
    }

    private fun getTextWatcher(et : EditText) : TextWatcher {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.apply {
                    if (count != 0 && checkFormatNecessity(s)) {
                        when (s.length) {
                            7 -> et.addSymbols(")-")
                            12, 15 -> et.addSymbols("-")
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (!isPhoneNumberValid(it)) {
                        et.error = "Phone number is not correct"
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

    private fun checkText() {
        with(binding) {
            etPhoneNumber.apply {
                setOnClickListener {
                    if (text.isEmpty()) addSymbols("+7 (9")
                }
                addTextChangedListener(getTextWatcher(this@apply))
            }
        }
    }

    private fun checkQuestionNumber() : Boolean {
        with(binding) {
            etQuestionNumb.apply {
                val questionNumber = text.toString().toInt()
                return if (questionNumber !in 1..20) {
                    error = "Pick the number between 1 and 20"
                    false
                } else true
            }
        }
    }

    companion object {
        const val FIRST_FRAGMENT_TAG = "FIRST_FRAGMENT"
    }


}