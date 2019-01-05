package com.github.windsekirun.naraeaudiorecorder.extensions

import android.os.Handler
import android.os.Looper

/**
 * internal extensions for run [action] on Ui Thread
 */
internal fun runOnUiThread(action: () -> Unit) = Handler(Looper.getMainLooper()).post(action)