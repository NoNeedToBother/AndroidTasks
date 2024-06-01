package ru.kpfu.itis.paramonov.androidtasks.domain.usecase

import ru.kpfu.itis.paramonov.androidtasks.presentation.model.ContactUiModel
import javax.inject.Inject

class GetContactsUseCase @Inject constructor() {
    operator fun invoke(): List<ContactUiModel> {
        return listOf(
            ContactUiModel("Vasya Pupkin", "88005553535"),
            ContactUiModel("Vasiliy Pupkin", "88005553535"),
            ContactUiModel("Vasya", "88005553535"),
            ContactUiModel("Pupkin", "88005553535")
        )
    }
}