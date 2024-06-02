package ru.kpfu.itis.paramonov.androidtasks.data.repository

import android.content.Context
import android.provider.ContactsContract
import ru.kpfu.itis.paramonov.androidtasks.domain.model.contact.ContactDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.ContactProvider
import javax.inject.Inject

class ContactProviderImpl @Inject constructor(
    private val context: Context
): ContactProvider {
    override fun provide(): List<ContactDomainModel> {
        val resolver = context.contentResolver
        val uri = ContactsContract.Contacts.CONTENT_URI
        val contacts = mutableListOf<ContactDomainModel>()
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )

        resolver.query(
            uri, projection, null, null, CONTACT_NAME_COL
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndex(CONTACT_ID_COL)
            val nameIndex = cursor.getColumnIndex(CONTACT_NAME_COL)
            while(cursor.moveToNext()) {
                if (idIndex >= 0 && nameIndex >= 0) {
                    val contactId = cursor.getString(idIndex)
                    val displayName = cursor.getString(nameIndex)

                    val phoneProjection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    resolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        phoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(contactId),
                        null
                    )?.use {
                        val phoneIndex = it.getColumnIndex(CONTACT_PHONE_COL)
                        if (it.moveToFirst() && phoneIndex >= 0) {
                            val phone = it.getString(phoneIndex)
                            contacts.add(ContactDomainModel(name = displayName, phone = phone))
                        }
                    }
                }
            }
        }
        return contacts
    }

    companion object {
        private const val CONTACT_ID_COL = ContactsContract.Contacts._ID
        private const val CONTACT_NAME_COL = ContactsContract.Contacts.DISPLAY_NAME
        private const val CONTACT_PHONE_COL = ContactsContract.CommonDataKinds.Phone.NUMBER
    }
}