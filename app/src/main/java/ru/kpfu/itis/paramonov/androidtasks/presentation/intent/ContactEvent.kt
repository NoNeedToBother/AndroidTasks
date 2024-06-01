package ru.kpfu.itis.paramonov.androidtasks.presentation.intent

import ru.kpfu.itis.paramonov.androidtasks.presentation.model.ContactUiModel

sealed class ContactEvent {
    object OnGet : ContactEvent()
    data class OnChosen(val contact: ContactUiModel): ContactEvent()
}