package ru.kpfu.itis.paramonov.androidtasks.domain.usecase

import ru.kpfu.itis.paramonov.androidtasks.domain.mapper.ContactUiModelMapper
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.ContactProvider
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.ContactUiModel
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val contactProvider: ContactProvider,
    private val mapper: ContactUiModelMapper
) {
    operator fun invoke(): List<ContactUiModel> {
        return contactProvider.provide().map { contact ->
            mapper.map(contact)
        }
    }
}