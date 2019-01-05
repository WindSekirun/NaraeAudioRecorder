package com.github.windsekirun.naraeaudiorecorder.source

import android.media.AudioRecord
import android.media.MediaSyncEvent
import android.media.audiofx.AutomaticGainControl
import android.media.audiofx.NoiseSuppressor
import android.util.Log
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig
import com.github.windsekirun.naraeaudiorecorder.constants.LogConstants

/**
 * Default setting + AutomaticGainControl for [AudioRecord]
 */
class AutomaticGainAudioSource(audioRecordConfig: AudioRecordConfig = AudioRecordConfig.defaultConfig())
    : DefaultAudioSource(audioRecordConfig) {

    override fun preProcessAudioRecord(mediaSyncEvent: MediaSyncEvent?): AudioRecord {
        if (!NoiseSuppressor.isAvailable()) {
            Log.e(LogConstants.TAG, LogConstants.EXCEPTION_NOT_SUPPORTED_AUTOMATIC_GAIN_CONTROL)
            throw UnsupportedOperationException(LogConstants.EXCEPTION_NOT_SUPPORTED_AUTOMATIC_GAIN_CONTROL)
        }

        val automaticGainControl = AutomaticGainControl.create(getAudioRecord().audioSessionId)
        if (automaticGainControl != null) {
            automaticGainControl.enabled = true
        } else {
            Log.e(LogConstants.TAG, LogConstants.EXCEPTION_INITIAL_FAILED_AUTOMATIC_GAIN_CONTROL)
        }

        return super.preProcessAudioRecord(mediaSyncEvent)
    }
}