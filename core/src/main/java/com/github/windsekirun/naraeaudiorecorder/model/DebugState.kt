package com.github.windsekirun.naraeaudiorecorder.model

import android.util.Log
import com.github.windsekirun.naraeaudiorecorder.constants.LogConstants

/**
 * Display debug log in inside of Library
 */
object DebugState {
    var state: Boolean = false

    /**
     * Display debug log
     */
    fun debug(message: String) {
        if (!state) return
        Log.d(LogConstants.TAG, message)
    }

    /**
     * Display error log
     */
    fun error(message: String, exception: Exception? = null) {
        if (!state) return
        if (exception != null) {
            Log.e(LogConstants.TAG, message, exception);
        } else {
            Log.e(LogConstants.TAG, message)
        }
    }
}