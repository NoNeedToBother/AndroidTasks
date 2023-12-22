package ru.kpfu.itis.paramonov.androidtasks.model

import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.FilmEntity

data class Film(
    val id: Int? = null,
    val title: String,
    val description: String,
    val releaseDate: String,
    val posterUrl: String
): RvModel(){
    private var isFromLikedList = false

    fun setFromLiked(): Film {
        isFromLikedList = true
        return this
    }

    fun isFromLiked(): Boolean {
        return isFromLikedList
    }
    companion object {
        fun getFromEntity(entity: FilmEntity): Film {
            return Film(entity.filmId,
                entity.filmTitle,
                entity.filmDescription,
                entity.filmReleaseDate,
                entity.filmPosterURL)
        }
    }
}