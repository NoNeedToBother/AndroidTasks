package ru.kpfu.itis.paramonov.androidtasks.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AirplaneModeBroadcastReceiver: BroadcastReceiver(){
    private var listener: OnAirplaneModeChangedListener? = null

    interface OnAirplaneModeChangedListener {
        fun onAirplaneModeChanged(airplaneModeStatus: String)
    }

    fun setOnAirplaneModeChangedListener(listener: OnAirplaneModeChangedListener) {
        this.listener = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.run {
            if (action.equals(AIRPLANE_MODE_INTENT)) {
                extras?.getBoolean(AIRPLANE_MODE_KEY)?.let {
                    if (it) listener?.onAirplaneModeChanged(AIRPLANE_MODE_ON)
                    else listener?.onAirplaneModeChanged(AIRPLANE_MODE_OFF)
                }
            }
        }
    }

    companion object {
        const val AIRPLANE_MODE_ON = "on"
        const val AIRPLANE_MODE_OFF = "off"

        private const val AIRPLANE_MODE_INTENT = "android.intent.action.AIRPLANE_MODE"
        private const val AIRPLANE_MODE_KEY = "state"
    }

}