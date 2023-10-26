package ru.kpfu.itis.paramonov.androidtasks.model

import java.io.Serializable

data class Question(val text : String, val answer : Int, val options : List<Option>) : Serializable {

}
