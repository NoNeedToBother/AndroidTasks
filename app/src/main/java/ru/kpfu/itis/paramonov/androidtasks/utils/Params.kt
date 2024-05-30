package ru.kpfu.itis.paramonov.androidtasks.utils

object Params {
    const val CITY_EMPTY_DATA = ""
    const val WEATHER_EMPTY_DATA = ""
    const val METRIC_UNITS_KEY = "metric"
    const val NO_TEMPERATURE_DATA = -999.0
    const val NO_COORD_DATA = -999.0

    const val RESPONSE_INFO_CODE_LOWER_BOUND = 100
    const val RESPONSE_INFO_CODE_UPPER_BOUND = 199

    const val RESPONSE_SUCCESS_CODE_LOWER_BOUND = 200
    const val RESPONSE_SUCCESS_CODE_UPPER_BOUND = 299

    const val RESPONSE_REDIRECTION_CODE_LOWER_BOUND = 300
    const val RESPONSE_REDIRECTION_CODE_UPPER_BOUND = 399

    const val RESPONSE_CLIENT_ERROR_CODE_LOWER_BOUND = 400
    const val RESPONSE_CLIENT_ERROR_CODE_UPPER_BOUND = 499

    const val RESPONSE_SERVER_ERROR_CODE_LOWER_BOUND = 500
    const val RESPONSE_SERVER_ERROR_CODE_UPPER_BOUND = 599

    const val REQUEST_EMPTY_BODY = "No request body data"
    const val RESPONSE_EMPTY_BODY = "No response body data"
    const val HEADERS_EMPTY = "No headers data"
}