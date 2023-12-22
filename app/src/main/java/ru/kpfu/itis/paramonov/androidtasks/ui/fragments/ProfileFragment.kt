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
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentProfileBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.model.User
import ru.kpfu.itis.paramonov.androidtasks.util.ParamKeys
import ru.kpfu.itis.paramonov.androidtasks.util.PasswordUtil

class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    private var correctPassword = false
    private var correctConfirmPassword = false
    private var correctPhoneNum = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUserInfo()
        setOnTextChangedListeners()
        setOnClickListeners()
    }

    private fun setUserInfo() {
        with(binding) {
            lifecycleScope.launch(Dispatchers.IO) {
                val entity = ServiceLocator.getDbInstance().userDao.getUserById(getUserId())
                var user: User? = null
                entity?.let {
                    user = User.getFromEntity(entity)
                }

                withContext(Dispatchers.Main) {
                    etEmail.hint = user?.email
                    etName.hint = user?.email
                    etPhone.hint = user?.phoneNumber
                }
            }
        }
    }

    private fun setOnClickListeners() {
        with (binding) {
            btnSavePassword.setOnClickListener {
                if (!(correctPassword && correctConfirmPassword)) {
                    showToast(R.string.incorrect_info)
                    return@setOnClickListener
                }
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()
                if (password != confirmPassword) {
                    showToast(R.string.password_no_match)
                }
                lifecycleScope.launch(Dispatchers.IO) {
                    val userEntity = ServiceLocator.getDbInstance().userDao.getUserWithPassword(
                        getUserId(), PasswordUtil.encrypt(password))
                    if (userEntity.isEmpty()) {
                        withContext(Dispatchers.Main) {
                            showToast(R.string.incorrect_password)
                        }
                    } else {
                        ServiceLocator.getDbInstance().userDao.updateUserPassword(
                            PasswordUtil.encrypt(confirmPassword), getUserId())
                    }
                }
            }

            btnSavePhone.setOnClickListener {
                if (!correctPhoneNum) {
                    showToast(R.string.incorrect_info)
                    return@setOnClickListener
                }
                val phoneNumber = etPhone.text.toString()
                lifecycleScope.launch(Dispatchers.IO) {
                    val userEntities = ServiceLocator.getDbInstance().userDao.getUserByPhoneNumber(phoneNumber)
                    if (userEntities.isNotEmpty()) {
                        withContext(Dispatchers.Main) {
                            showToast(R.string.phone_exists)
                        }
                    } else {
                        ServiceLocator.getDbInstance().userDao.updateUserPhoneNumber(phoneNumber, getUserId())
                    }
                }
            }

            btnLogout.setOnClickListener {
                logoutUser()
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }

            btnDeleteAccount.setOnClickListener {
                val userId = getUserId()
                logoutUser()
                lifecycleScope.launch(Dispatchers.IO) {
                    ServiceLocator.getDbInstance().userDao.deleteUserById(userId)
                }
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }
    }

    private fun logoutUser() {
        ServiceLocator.getSharedPreferences().edit().apply {
            clear()
            apply()
        }
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

            etConfirmPassword.addTextChangedListener(getTextWatcher {
                with (it.toString()) {
                    if (length < MIN_PASSWORD_LEN) {
                        correctConfirmPassword = false
                        etPassword.error = getString(R.string.password_error)
                    }
                    else {
                        correctConfirmPassword = true
                        etPassword.error = null
                    }
                }
            })

            etPhone.addTextChangedListener(getTextWatcher {
                if (!Patterns.PHONE.matcher(it.toString()).matches()) {
                    correctPhoneNum = false
                    etPhone.error = getString(R.string.phone_num_error)
                } else {
                    correctPhoneNum = true
                    etPhone.error = null
                }
            })
        }
    }

    private fun getTextWatcher(afterTextChanged: (editable: Editable) -> Unit) = object :
        TextWatcher {
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

    private fun getUserId(): Int {
        return ServiceLocator.getSharedPreferences().getInt(
            ParamKeys.SHARED_PREF_USER_ID_KEY,
            ParamKeys.NO_USER_ID
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val MIN_PASSWORD_LEN = 8
    }
}