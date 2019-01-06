package com.github.windsekirun.naraeaudiorecorder.extensions

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.windsekirun.naraeaudiorecorder.constants.LogConstants

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
        Log.e(LogConstants.TAG, "Catch an exception. ${t.message}", t)
        null
    }
}