package ru.kpfu.itis.paramonov.androidtasks.data.handler

import retrofit2.HttpException
import ru.kpfu.itis.paramonov.androidtasks.utils.ResManager

class ExceptionHandlerDelegate(
    private val resManager: ResManager,
) {

    fun handleException(ex: Throwable): Throwable {
        return ex
        /*
        return when (ex) {
            is HttpException -> {
                /*
                when (ex.code()) {
                    401 -> {
                        UserNotAuthorizedException(message = resManager.getString(R.string.user_not_authorized))
                    }
                    403 -> {
                        ex
                    }
                    429 -> {
                        TooManyRequestsException(message = resManager.getString(R.string.too_many_requests))
                    }
                    else -> {
                        ex
                    }
                }*/
            }
            else -> {
                ex
            }
        }*/
    }
}