package ru.kpfu.itis.paramonov.androidtasks.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.FilmEntity

@Dao
interface FilmDao {
    @Query("SELECT * FROM films")
    fun getAllFilms(): List<FilmEntity>

    @Query("SELECT * FROM films WHERE film_id = :filmId")
    fun getFilmById(filmId: Int): FilmEntity?


    @Query("DELETE FROM films WHERE film_id = :filmId")
    fun deleteFilmById(filmId: Int)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveFilm(filmEntity: FilmEntity)

    @Query("SELECT * FROM films WHERE film_title = :title AND film_release_date = :date")
    fun getFilmByTitleAndDate(title: String, date: String): FilmEntity?

    @Query("SELECT * FROM films WHERE film_id IN (:filmIds)")
    fun getFilmsById(filmIds: List<Int>): List<FilmEntity>
}