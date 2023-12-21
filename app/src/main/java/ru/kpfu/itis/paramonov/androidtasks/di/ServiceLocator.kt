package ru.kpfu.itis.paramonov.androidtasks.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import ru.kpfu.itis.paramonov.androidtasks.data.db.ApplicationDatabase

object ServiceLocator {

    private var dbInstance: ApplicationDatabase? = null

    private var applicationPref: SharedPreferences? = null

    private const val DB_NAME = "films.db"

    fun initData(ctx: Context) {
        dbInstance = Room.databaseBuilder(ctx, ApplicationDatabase::class.java, DB_NAME)
            .build()

        applicationPref = ctx.getSharedPreferences("films_pref", Context.MODE_PRIVATE)
    }

    fun getDbInstance(): ApplicationDatabase {
        return dbInstance ?: throw IllegalStateException("Db not initialized")
    }

    fun getSharedPreferences(): SharedPreferences {
        return applicationPref ?: throw IllegalStateException("Preferences not initialized")
    }
}