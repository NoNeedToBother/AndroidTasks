package ru.kpfu.itis.paramonov.androidtasks.domain.repository

import ru.kpfu.itis.paramonov.androidtasks.domain.model.contact.ContactDomainModel

interface ContactProvider {

    fun provide(): List<ContactDomainModel>
}