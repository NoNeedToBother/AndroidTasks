package ru.kpfu.itis.paramonov.androidtasks.presentation.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.ContactUiModel

@Stable
data class ContactsScreenViewState(
    val contacts: State<List<ContactUiModel>>
)