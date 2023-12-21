package ru.kpfu.itis.paramonov.androidtasks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films")
data class FilmEntity(
    @PrimaryKey @ColumnInfo(name = "film_id")
    val filmId: Int,
    @ColumnInfo(name = "film_title")
    val filmTitle: String,
    @ColumnInfo(name = "film_description")
    val filmDescription : String,
    @ColumnInfo(name = "film_release_date")
    val filmReleaseDate : Long
)