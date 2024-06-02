package ru.kpfu.itis.paramonov.androidtasks.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.kpfu.itis.paramonov.androidtasks.R

abstract class BaseBottomSheetDialogFragment: DefaultFragment, BottomSheetDialogFragment() {
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

    abstract override fun init()

    abstract override fun observeData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeData()
    }
}