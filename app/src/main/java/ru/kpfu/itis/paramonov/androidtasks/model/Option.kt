package ru.kpfu.itis.paramonov.androidtasks.model

data class Option(val option : Int, var checked : Boolean = false) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Option

        if (option != other.option) return false

        return true
    }


}
