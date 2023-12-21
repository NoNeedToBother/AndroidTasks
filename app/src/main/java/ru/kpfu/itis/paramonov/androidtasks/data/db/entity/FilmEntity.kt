package ru.kpfu.itis.paramonov.androidtasks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kpfu.itis.paramonov.androidtasks.model.Film

@Entity(tableName = "films")
data class FilmEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "film_id")
    val filmId: Int = 0,
    @ColumnInfo(name = "film_title")
    val filmTitle: String,
    @ColumnInfo(name = "film_description")
    val filmDescription: String,
    @ColumnInfo(name = "film_release_date")
    val filmReleaseDate: String,
    @ColumnInfo(name = "film_poster_url")
    val filmPosterURL: String
) {
    companion object {
        fun getEntity(film: Film): FilmEntity {
            return FilmEntity(
                filmTitle = film.title,
                filmDescription = film.description,
                filmReleaseDate = film.releaseDate,
                filmPosterURL = film.posterUrl
            )
        }
    }
}