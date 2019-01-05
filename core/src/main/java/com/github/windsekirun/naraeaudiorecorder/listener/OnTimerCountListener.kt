package com.github.windsekirun.naraeaudiorecorder.listener

/**
 * Listener for handling timer
 */
interface OnTimerCountListener {

    /**
     * Invoke when every [com.github.windsekirun.naraeaudiorecorder.config.AudioRecorderConfig.refreshTimerMillis] is passed
     */
    fun onTime(currentTime: Long = 0, maxTime: Long = 0)
}