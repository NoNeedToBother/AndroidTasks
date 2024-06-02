package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.intent

sealed class ContactEvent {
    object OnGet : ContactEvent()
}