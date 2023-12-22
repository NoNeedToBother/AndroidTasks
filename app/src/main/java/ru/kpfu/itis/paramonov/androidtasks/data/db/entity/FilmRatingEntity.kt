package ru.kpfu.itis.paramonov.androidtasks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_film_ratings",
    primaryKeys = ["user_id", "film_id"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            childColumns = ["user_id"],
            parentColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FilmEntity::class,
            childColumns = ["film_id"],
            parentColumns = ["film_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FilmRatingEntity(
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "film_id")
    val filmId: Int,
    @ColumnInfo(name = "rating")
    val rating: Int?,
    @ColumnInfo(name = "is_liked")
    val liked: Boolean?
) {
    companion object {
        fun getEntity(userId: Int, filmId: Int, rating: Int?, isLiked: Boolean?): FilmRatingEntity {
            return FilmRatingEntity(
                userId, filmId, rating, isLiked
            )
        }
    }
}