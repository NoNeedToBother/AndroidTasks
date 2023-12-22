package ru.kpfu.itis.paramonov.androidtasks.model

import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.UserEntity

data class User(
    val name: String,
    val phoneNumber: String,
    val email: String,
    val password: String
) {
    companion object {
        fun getFromEntity(entity: UserEntity): User {
            return User(
                entity.name,
                entity.phoneNumber,
                entity.emailAddress,
                entity.password
            )
        }
    }
}