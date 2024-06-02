package ru.kpfu.itis.paramonov.androidtasks.presentation.base

import androidx.compose.ui.platform.ComposeView

interface DefaultFragment {

    fun layout(): Int?

    fun composeView(): ComposeView?

    fun init()

    fun observeData()
}