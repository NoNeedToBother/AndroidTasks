package ru.kpfu.itis.paramonov.androidtasks.model

import java.io.Serializable

data class CityFact(val id : Int, val city: String, val title: String, val content: String, var isLiked : Boolean = false) : Model(), Serializable
