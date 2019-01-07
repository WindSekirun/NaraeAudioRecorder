package com.github.windsekirun.naraeaudiorecorder.source

import android.media.AudioRecord
import android.media.audiofx.NoiseSuppressor
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig
import com.github.windsekirun.naraeaudiorecorder.constants.LogConstants
import com.github.windsekirun.naraeaudiorecorder.model.DebugState

/**
 * Default setting + NoiseSuppressor for [AudioRecord]
 */
class NoiseAudioSource(audioRecordConfig: AudioRecordConfig = AudioRecordConfig.defaultConfig())
    : DefaultAudioSource(audioRecordConfig) {

    override fun preProcessAudioRecord(): AudioRecord {
        if (!NoiseSuppressor.isAvailable()) {
            DebugState.error(LogConstants.EXCEPTION_NOT_SUPPORTED_NOISE_SUPPRESSOR)
            throw UnsupportedOperationException(LogConstants.EXCEPTION_NOT_SUPPORTED_NOISE_SUPPRESSOR)
        }

        val noiseSuppressor = NoiseSuppressor.create(getAudioRecord().audioSessionId)
        if (noiseSuppressor != null) {
            noiseSuppressor.enabled = true
        } else {
            DebugState.error(LogConstants.EXCEPTION_INITIAL_FAILED_NOISE_SUPPESSOR)
        }

        return super.preProcessAudioRecord()
    }
}