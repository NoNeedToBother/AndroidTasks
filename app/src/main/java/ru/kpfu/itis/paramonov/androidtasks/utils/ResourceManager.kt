package ru.kpfu.itis.paramonov.androidtasks.utils

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface ResourceManager {

    fun getString(@StringRes res: Int): String

    fun getString(@StringRes res: Int, vararg args: Any?): String

    fun getColor(@ColorRes res: Int): Int

}