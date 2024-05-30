package ru.kpfu.itis.paramonov.androidtasks.data.response

import ru.kpfu.itis.paramonov.androidtasks.data.model.response.ResponseData

class ResponseDataLog {
    private val _responseDataList: MutableList<ResponseData> = ArrayList()

    val responseDataList: List<ResponseData> get() = _responseDataList

    fun addResponse(responseData: ResponseData) {
        if (_responseDataList.size >= RESPONSE_DATA_AMOUNT_LIMIT) {
            _responseDataList.removeFirst()
        }
        _responseDataList.add(responseData)
    }

    companion object {
        private const val RESPONSE_DATA_AMOUNT_LIMIT = 50
    }

}