package ru.kpfu.itis.paramonov.androidtasks.data.handler

import retrofit2.HttpException
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.data.exceptions.BadRequestException
import ru.kpfu.itis.paramonov.androidtasks.data.exceptions.CityNotFoundException
import ru.kpfu.itis.paramonov.androidtasks.data.exceptions.DefaultException
import ru.kpfu.itis.paramonov.androidtasks.data.exceptions.ServerErrorException
import ru.kpfu.itis.paramonov.androidtasks.data.exceptions.TooManyRequestsException
import ru.kpfu.itis.paramonov.androidtasks.data.exceptions.UserNotAuthorizedException
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager
import javax.inject.Inject

class ExceptionHandlerDelegate @Inject constructor(
    private val resourceManager: ResourceManager,
) {

    fun handleException(ex: Throwable): Throwable {
        return when (ex) {
            is HttpException -> {
                when (ex.code()) {
                    400 -> BadRequestException(resourceManager.getString(R.string.bad_request))
                    401 ->
                        UserNotAuthorizedException(resourceManager.getString(R.string.user_not_authorized))
                    404 ->
                        CityNotFoundException(resourceManager.getString(R.string.city_not_found))
                    429 -> TooManyRequestsException(resourceManager.getString(R.string.too_many_requests))
                    in 500..599 ->
                        ServerErrorException(resourceManager.getString(R.string.server_error))
                    else -> DefaultException(resourceManager.getString(R.string.default_exception))
                }
            }
            else -> DefaultException(resourceManager.getString(R.string.default_exception))
        }
    }
}