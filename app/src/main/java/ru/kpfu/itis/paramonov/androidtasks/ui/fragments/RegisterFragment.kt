package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.UserEntity
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentRegisterBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.model.User
import ru.kpfu.itis.paramonov.androidtasks.util.PasswordUtil

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding get() = _binding!!

    private var correctPassword = false
    private var correctEmail = false
    private var correctPhoneNum = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setOnTextChangedListeners()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnRegister.setOnClickListener {
                if (!(correctEmail && correctPhoneNum && correctPassword)) {
                    showToast(R.string.incorrect_info)
                    return@setOnClickListener
                }
                val phoneNumber = etPhoneNum.text.toString()
                if (!checkPhoneNumber(phoneNumber)) {
                    showToast(R.string.phone_exists)
                    return@setOnClickListener
                }
                val email = etEmail.text.toString()
                if (!checkEmail(email)) {
                    showToast(R.string.email_exists)
                    return@setOnClickListener
                }

                val name = etName.text.toString()
                val password = etPassword.text.toString()

                val user = User(name, phoneNumber, email, PasswordUtil.encrypt(password))
                lifecycleScope.launch(Dispatchers.IO) {
                    ServiceLocator.getDbInstance().userDao.saveUser(UserEntity.getEntity(user))
                }
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    private fun checkPhoneNumber(phoneNumber: String): Boolean {
        var res = true
        lifecycleScope.launch(Dispatchers.IO) {
            val userEntity = ServiceLocator.getDbInstance().userDao.getUserByPhoneNumber(phoneNumber)
            if (userEntity != null) {
                res = false
            }
        }
        return res
    }

    private fun checkEmail(email: String): Boolean {
        var res = true
        lifecycleScope.launch(Dispatchers.IO) {
            val userEntity = ServiceLocator.getDbInstance().userDao.getUserByEmail(email)
            if (userEntity != null) res = false
        }
        return res
    }

    private fun setOnTextChangedListeners() {
        with(binding) {
            etPassword.addTextChangedListener(getTextWatcher {
                with (it.toString()) {
                    if (length < MIN_PASSWORD_LEN) {
                        correctPassword = false
                        etPassword.error = getString(R.string.password_error)
                    }
                    else {
                        correctPassword = true
                        etPassword.error = null
                    }
                }
            })

            etEmail.addTextChangedListener(getTextWatcher {
                if (!Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
                    correctEmail = false
                    etEmail.error = getString(R.string.email_error)
                } else {
                    correctEmail = true
                    etEmail.error = null
                }
            })

            etPhoneNum.addTextChangedListener(getTextWatcher {
                if (!Patterns.PHONE.matcher(it.toString()).matches()) {
                    correctPhoneNum = false
                    etPhoneNum.error = getString(R.string.phone_num_error)
                } else {
                    correctPhoneNum = true
                    etPhoneNum.error = null
                }
            })
        }
    }

    private fun getTextWatcher(afterTextChanged: (editable: Editable) -> Unit) = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            p0?.let {
                afterTextChanged.invoke(it)
            }
        }
    }

    private fun showToast(messageId: Int) {
        Toast.makeText(context, messageId, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val MIN_PASSWORD_LEN = 8
    }
}