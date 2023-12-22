package ru.kpfu.itis.paramonov.androidtasks.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.FilmRatingEntity

@Dao
interface FilmRatingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFilmRating(filmRatingEntity: FilmRatingEntity)

    @Query("SELECT * from user_film_ratings WHERE user_id = :userId AND film_id = :filmId")
    fun getFilmRating(userId: Int, filmId: Int): FilmRatingEntity?

    @Query("UPDATE user_film_ratings SET rating = :rating WHERE user_id = :userId AND film_id = :filmId")
    fun updateMovieRating(userId: Int, filmId: Int, rating: Int)

    @Query("UPDATE user_film_ratings SET is_liked = :isLiked WHERE user_id = :userId AND film_id = :filmId")
    fun updateMovieLiked(userId: Int, filmId: Int, isLiked: Boolean)

    @Query("SELECT rating from user_film_ratings WHERE film_id = :filmId")
    fun getFilmRatings(filmId : Int) : List<Int?>

    @Query("SELECT rating from user_film_ratings WHERE user_id = :userId")
    fun getUserRatings(userId : Int) : List<Int>

    @Query("SELECT film_id from user_film_ratings WHERE user_id = :userId AND is_liked = 1")
    fun getUserLikedFilms(userId: Int) : List<Int>
}