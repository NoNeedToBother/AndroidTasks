package ru.kpfu.itis.paramonov.androidtasks.presentation.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.R

abstract class BaseFragment(@LayoutRes layoutId: Int): Fragment(layoutId) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    protected abstract fun initView()

    protected abstract fun observeData()

    protected fun showError(error: Throwable) {
        val errorMessage = error.message ?: getString(R.string.default_exception)
        Toast.makeText(
            requireContext(),
            errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

}