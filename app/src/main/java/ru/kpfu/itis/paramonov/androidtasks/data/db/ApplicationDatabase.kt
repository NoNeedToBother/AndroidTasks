package ru.kpfu.itis.paramonov.androidtasks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kpfu.itis.paramonov.androidtasks.data.db.dao.FilmDao
import ru.kpfu.itis.paramonov.androidtasks.data.db.dao.FilmRatingsDao
import ru.kpfu.itis.paramonov.androidtasks.data.db.dao.UserDao
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.UserEntity
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.FilmRatingEntity

@Database(
    entities = [UserEntity::class, FilmEntity::class, FilmRatingEntity::class],
    version = 1
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val filmDao: FilmDao
    abstract val filmRatingsDao: FilmRatingsDao
}