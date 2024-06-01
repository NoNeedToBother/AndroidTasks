package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.domain.usecase.GetContactsUseCase
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseMviViewModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.intent.ContactEvent
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.ContactUiModel
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase
): BaseMviViewModel<ContactEvent>() {

    private val _contactsDataFlow = MutableStateFlow(listOf<ContactUiModel>())

    val contactsDataFlow: StateFlow<List<ContactUiModel>> get() = _contactsDataFlow

    override fun onEvent(event: ContactEvent) {
        when(event) {
            is ContactEvent.OnGet -> getContacts()
            is ContactEvent.OnChosen -> onContactChosen(event.contact)
        }
    }

    private fun getContacts() {
        viewModelScope.launch {
            val contacts = getContactsUseCase.invoke()
            _contactsDataFlow.value = contacts
        }
    }

    private fun onContactChosen(contact: ContactUiModel) {

    }
}