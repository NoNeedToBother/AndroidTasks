package ru.kpfu.itis.paramonov.androidtasks.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.R

abstract class BaseFragment(): Fragment(), DefaultFragment {

    @LayoutRes
    override fun layout(): Int? = null

    override fun composeView(): ComposeView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return layout()?.let { layoutRes ->
            inflater.inflate(layoutRes, container, false)
        }
            ?: composeView()
            ?: throw IllegalStateException(getString(R.string.no_fragment_view))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeData()
    }

    abstract override fun init()

    abstract override fun observeData()

    protected fun showError(error: Throwable) {
        val errorMessage = error.message ?: getString(R.string.default_exception)
        showToast(errorMessage)
    }

    protected fun showError(error: String) {
        showToast(error)
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT
        ).show()
    }

}