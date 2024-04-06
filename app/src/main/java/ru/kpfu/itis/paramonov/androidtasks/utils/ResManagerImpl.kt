package ru.kpfu.itis.paramonov.androidtasks.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class ResManagerImpl @Inject constructor(
    private val ctx: Context,
) : ResManager {

    override fun getString(@StringRes res: Int): String = ctx.resources.getString(res)

    override fun getString(@StringRes res: Int, vararg args: Any?): String {
        return ctx.resources.getString(res, *args)
    }
}