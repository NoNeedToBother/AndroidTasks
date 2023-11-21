package ru.kpfu.itis.paramonov.androidtasks.util

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.model.CoroutineConfig
import kotlin.random.Random

class CoroutineHandler(private val activity: AppCompatActivity) {

    private val stopOnBackground = CoroutineConfig.stopOnBackground
    private val async = CoroutineConfig.isAsync
    private val coroutineAmount = CoroutineConfig.coroutineAmount
    private var doneCoroutines = 0

    private var job: Job? = null

    interface OnExecutionEndedListener {
        fun onExecutionEnded(executionStatus: Int)
    }

    private var listener: OnExecutionEndedListener? = null

    fun setOnExecutionEndedListener(listener: OnExecutionEndedListener) {
        this.listener = listener
    }


    fun execute() {
        with(activity) {
            job = lifecycleScope.launch() {
                repeat(coroutineAmount) {
                    if (async) {
                        launch { launchCoroutine() }
                    }
                    else launchCoroutine()
                }
            }.also { job ->
                job.invokeOnCompletion {
                    if (it == null) {
                        Log.e(LOG_COROUTINE_TAG, ALL_COROUTINES_ENDED_MSG)
                        listener?.onExecutionEnded(EXECUTION_STATUS_SUCCESS)
                    }
                    when(it) {
                        is CancellationException -> {
                            if (stopOnBackground) listener?.onExecutionEnded(EXECUTION_STATUS_STOPPED)
                            else listener?.onExecutionEnded(EXECUTION_STATUS_ERROR)
                        }
                        else -> listener?.onExecutionEnded(EXECUTION_STATUS_ERROR)
                    }
                }
            }
        }
    }

    private suspend fun launchCoroutine() {
        delay(getRandomDuration())
        doneCoroutines++
        Log.e(LOG_COROUTINE_TAG, COROUTINE_ENDED_MSG)
    }

    fun notifyActivityStopped() {
        if (stopOnBackground) {
            job?.cancel(CancellationException(CANCELLATION_EXCEPTION_MSG))
            Log.e(LOG_COROUTINE_TAG, SOME_COROUTINES_ENDED_MSG.format(doneCoroutines))
        }
    }

    private fun getRandomDuration(): Long {
        val rand = Random.nextInt(1, 5)
        return rand * 1000L
    }

    companion object {
        const val EXECUTION_STATUS_SUCCESS = 0
        const val EXECUTION_STATUS_STOPPED = 1
        const val EXECUTION_STATUS_ERROR = 2

        private const val LOG_COROUTINE_TAG = "COROUTINE"
        private const val COROUTINE_ENDED_MSG = "Coroutine is finished"
        private const val ALL_COROUTINES_ENDED_MSG = "All coroutines are finished"
        private const val SOME_COROUTINES_ENDED_MSG = "%s coroutines are finished"
        private const val CANCELLATION_EXCEPTION_MSG = "Coroutines execution is cancelled"
    }
}