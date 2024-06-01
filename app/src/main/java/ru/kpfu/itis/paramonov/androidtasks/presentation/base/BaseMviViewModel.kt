package ru.kpfu.itis.paramonov.androidtasks.presentation.base

abstract class BaseMviViewModel<E>: BaseViewModel() {

    abstract fun onEvent(event: E)
}