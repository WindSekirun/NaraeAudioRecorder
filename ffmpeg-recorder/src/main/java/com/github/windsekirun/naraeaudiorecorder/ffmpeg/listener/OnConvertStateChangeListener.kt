package com.github.windsekirun.naraeaudiorecorder.ffmpeg.listener

import com.github.windsekirun.naraeaudiorecorder.ffmpeg.model.FFmpegConvertState

/**
 * Listener for handling state changes
 */
interface OnConvertStateChangeListener {

    /**
     * Call when [FFmpegConvertState] is changed
     */
    fun onState(state: FFmpegConvertState)
}