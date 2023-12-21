package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.UserEntity
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentLoginBinding
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentRegisterBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.model.User
import ru.kpfu.itis.paramonov.androidtasks.util.PasswordUtil

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        checkSessionUser()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnRegister.setOnClickListener {
                findNavController().navigate(
                    R.id.action_loginFragment_to_registerFragment
                )
            }

            btnSignIn.setOnClickListener {
                val email = etEmail.text.toString()
                val password = PasswordUtil.encrypt(etPassword.text.toString())

                lifecycleScope.launch(Dispatchers.IO) {
                    val userEntity = ServiceLocator.getDbInstance().userDao.getUser(email, password)

                    if (userEntity != null) {
                        withContext(Dispatchers.Main) {
                            saveUserInSession(userEntity.id)
                            findNavController().navigate(R.id.action_loginFragment_to_filmsFragment)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, R.string.login_error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun checkSessionUser() {
        val userId = ServiceLocator.getSharedPreferences().getInt(SHARED_PREF_USER_ID_KEY, NO_USER_ID)
        if (userId != NO_USER_ID) {
            findNavController().navigate(R.id.action_loginFragment_to_filmsFragment)
        }
    }

    private fun saveUserInSession(userId: Int) {
        with(ServiceLocator.getSharedPreferences().edit()) {
            putInt(SHARED_PREF_USER_ID_KEY, userId)
            apply()
        }
    }

    companion object {
        private const val SHARED_PREF_USER_ID_KEY = "user_id"
        private const val NO_USER_ID = -1
    }
}