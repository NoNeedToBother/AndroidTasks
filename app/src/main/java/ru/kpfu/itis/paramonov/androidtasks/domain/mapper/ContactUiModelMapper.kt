package ru.kpfu.itis.paramonov.androidtasks.domain.mapper

import ru.kpfu.itis.paramonov.androidtasks.domain.model.contact.ContactDomainModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.ContactUiModel
import javax.inject.Inject

class ContactUiModelMapper @Inject constructor() {
    fun map(model: ContactDomainModel): ContactUiModel {
        return ContactUiModel(
            name = model.name, phone = model.phone
        )
    }
}