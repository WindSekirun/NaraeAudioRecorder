package com.github.windsekirun.naraeaudiorecorder.listener

import com.github.windsekirun.naraeaudiorecorder.model.RecordState

/**
 * NaraeAudioRecorder
 * Class: OnRecordStateChangeListener
 * Created by Pyxis on 1/5/19.
 *
 * Description:
 */

interface OnRecordStateChangeListener {

    fun onState(state: RecordState)
}