package com.github.windsekirun.naraeaudiorecorder.source

import android.media.AudioRecord
import android.media.MediaSyncEvent
import android.media.audiofx.NoiseSuppressor
import android.util.Log
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig
import com.github.windsekirun.naraeaudiorecorder.constants.LogConstants

/**
 * Default setting + NoiseSuppressor for [AudioRecord]
 */
class NoiseAudioSource(audioRecordConfig: AudioRecordConfig = AudioRecordConfig.defaultConfig())
    : DefaultAudioSource(audioRecordConfig) {

    override fun preProcessAudioRecord(mediaSyncEvent: MediaSyncEvent?): AudioRecord {
        if (!NoiseSuppressor.isAvailable()) {
            Log.e(LogConstants.TAG, LogConstants.EXCEPTION_NOT_SUPPORTED_NOISE_SUPPRESSOR)
            throw UnsupportedOperationException(LogConstants.EXCEPTION_NOT_SUPPORTED_NOISE_SUPPRESSOR)
        }

        val noiseSuppressor = NoiseSuppressor.create(getAudioRecord().audioSessionId)
        if (noiseSuppressor != null) {
            noiseSuppressor.enabled = true
        } else {
            Log.e(LogConstants.TAG, LogConstants.EXCEPTION_INITIAL_FAILED_NOISE_SUPPESSOR)
        }

        return super.preProcessAudioRecord(mediaSyncEvent)
    }
}