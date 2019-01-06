package com.github.windsekirun.naraeaudiorecorder.listener

import com.github.windsekirun.naraeaudiorecorder.model.RecordState

/**
 * Listener for handling state changes
 */
interface OnRecordStateChangeListener {

    /**
     * Call when [RecordState] is changed
     */
    fun onState(state: RecordState)
}