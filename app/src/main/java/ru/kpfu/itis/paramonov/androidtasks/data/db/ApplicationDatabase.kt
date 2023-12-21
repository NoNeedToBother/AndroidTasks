package ru.kpfu.itis.paramonov.androidtasks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kpfu.itis.paramonov.androidtasks.data.db.dao.FilmDao
import ru.kpfu.itis.paramonov.androidtasks.data.db.dao.UserDao
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.UserEntity
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.FilmEntity

@Database(
    entities = [UserEntity::class, FilmEntity::class],
    version = 1
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val filmDao: FilmDao
}