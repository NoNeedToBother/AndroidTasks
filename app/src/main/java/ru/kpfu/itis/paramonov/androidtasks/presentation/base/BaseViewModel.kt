package ru.kpfu.itis.paramonov.androidtasks.presentation.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    interface Result {
        interface Success<T> {
            fun getValue(): T
        }
        interface Failure {
            fun getException(): Throwable
        }
    }
}