package com.github.windsekirun.naraeaudiorecorder.extensions

import android.os.Handler
import android.os.Looper
import com.github.windsekirun.naraeaudiorecorder.model.DebugState

/**
 * internal extensions for run [action] on Ui Thread
 */
fun runOnUiThread(action: () -> Unit) = Handler(Looper.getMainLooper()).post(action)

/**
 * internal extensions for ignore exception in [action]
 */
internal inline fun <R> ignoreException(action: () -> R): R? {
    return try {
        action()
    } catch (t: Throwable) {
        DebugState.error("Catch an exception. ${t.message}", t)
        null
    }
}